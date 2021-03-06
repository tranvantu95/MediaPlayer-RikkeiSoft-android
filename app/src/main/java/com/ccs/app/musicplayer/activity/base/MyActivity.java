package com.ccs.app.musicplayer.activity.base;

import android.arch.lifecycle.Observer;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.ccs.app.musicplayer.R;
import com.ccs.app.musicplayer.activity.PlayerActivity;
import com.ccs.app.musicplayer.custom.view.BottomPlayerController;
import com.ccs.app.musicplayer.model.PlayerModel;
import com.ccs.app.musicplayer.model.item.SongItem;
import com.ccs.app.musicplayer.player.PlaylistPlayer;
import com.ccs.app.musicplayer.utils.Loader;

import java.util.List;

public abstract class MyActivity extends SwitchListActivity {

    protected BottomPlayerController bottomPlayerController;

    protected PlaylistPlayer.TogglePlayCallback togglePlayCallback;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void init() {
        super.init();

        togglePlayCallback = new PlaylistPlayer.TogglePlayCallback() {
            @Override
            public void onPlaylistEmpty(PlaylistPlayer playlistPlayer, List<SongItem> playlist) {
                handlePlaylistEmpty(playlistPlayer, true);
            }
        };

        bottomPlayerController = new BottomPlayerController((ViewGroup) findViewById(R.id.root),
                new BottomPlayerController.Callback() {
                    @Override
                    public void onTogglePlay() {
                        if(playlistPlayer != null)
                            playlistPlayer.togglePlay(true, togglePlayCallback);
                    }

                    @Override
                    public void onClick(View view) {
                        openPlayer();
                    }

                    @Override
                    public void onUpdateProgress(int progress) {
                        if(playlistPlayer != null) playlistPlayer.seekTo(progress);
                    }

                    @Override
                    public int calculate(CoordinatorLayout parent, View child, View dependency) {
                        return calculateForBottomPlayerController(parent, child, dependency);
                    }
                });
    }

    protected void openPlayer() {
        if(playlistPlayer != null) {
            if(playlistPlayer.getPlaylist().isEmpty())
                handlePlaylistEmpty(playlistPlayer, false);

            if(!playlistPlayer.getPlaylist().isEmpty())
                startActivity(PlayerActivity.createIntent(getApplicationContext()));
        }
    }

    protected void handlePlaylistEmpty(PlaylistPlayer playlistPlayer, boolean play) {
        playlistPlayer.handlePlaylistEmpty(getTitle2(), getPlaylistDefault(), play);
    }

    protected abstract List<SongItem> getPlaylistDefault();

    protected abstract int calculateForBottomPlayerController(CoordinatorLayout parent, View child, View dependency);

    @Override
    protected void onPlayerModelCreated(@NonNull PlayerModel _playerModel) {
        super.onPlayerModelCreated(_playerModel);

        playerModel.getCurrentSong().observe(this, new Observer<SongItem>() {
            @Override
            public void onChanged(@Nullable SongItem songItem) {
                if(songItem != null) {
                    bottomPlayerController.getTvTitle().setText(songItem.getName());
                    bottomPlayerController.getTvInfo().setText(songItem.getArtistName());

                    if(songItem.getBitmap() != null)
                        bottomPlayerController.getIvCover().setImageBitmap(songItem.getBitmap());
                    else bottomPlayerController.getIvCover().setImageResource(R.drawable.im_song);
                }
                else {
                    bottomPlayerController.getTvTitle().setText("");
                    bottomPlayerController.getTvInfo().setText("");
                    bottomPlayerController.getIvCover().setImageResource(R.drawable.im_song);
                }
            }
        });

        playerModel.getDuration().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer integer) {
                if(integer != null) bottomPlayerController.getSeekBar().setMax(integer);
            }
        });

        playerModel.getCurrentPosition().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer integer) {
                if(integer != null && !bottomPlayerController.isUserIsSeeking())
                    bottomPlayerController.getSeekBar().setProgress(integer);
            }
        });

        playerModel.getPlaying().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                if(aBoolean != null)
                    bottomPlayerController.getBtnPlay().setImageResource(aBoolean
                            ? R.drawable.ic_pause : R.drawable.ic_play);
            }
        });
    }

    @Override
    protected void onPermissionGranted() {
        super.onPermissionGranted();
        loadData();
    }

    @Override
    protected void onReceiverMediaChange() {
        super.onReceiverMediaChange();
        beforeReloadData();
        loadData();
    }

    private void loadData() {
        if(isDataLoaded()) onDataLoaded();
        else new Thread(new Runnable() {
            @Override
            public void run() {
                onLoadData();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        onDataLoaded();
                    }
                });
            }
        }).start();
    }

    protected abstract void beforeReloadData();

    protected boolean isDataLoaded() {
        return Loader.getInstance().isLoaded();
    }

    protected void onLoadData() {
        Log.d("debug", "onLoadData " + getClass().getSimpleName());
        Loader.getInstance().loadAll();
    }

    protected abstract void onDataLoaded();

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
