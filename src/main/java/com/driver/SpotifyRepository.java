package com.driver;

import java.util.*;

import org.springframework.stereotype.Repository;

@Repository
public class SpotifyRepository {
    public HashMap<Artist, List<Album>> artistAlbumMap;
    public HashMap<Album, List<Song>> albumSongMap;
    public HashMap<Playlist, List<Song>> playlistSongMap;
    public HashMap<Playlist, List<User>> playlistListenerMap;
    public HashMap<User, Playlist> creatorPlaylistMap;
    public HashMap<User, List<Playlist>> userPlaylistMap;
    public HashMap<Song, List<User>> songLikeMap;

    public List<User> users;
    public List<Song> songs;
    public List<Playlist> playlists;
    public List<Album> albums;
    public List<Artist> artists;

    public SpotifyRepository(){
        //To avoid hitting apis multiple times, initialize all the hashmaps here with some dummy data
        artistAlbumMap = new HashMap<>();
        albumSongMap = new HashMap<>();
        playlistSongMap = new HashMap<>();
        playlistListenerMap = new HashMap<>();
        creatorPlaylistMap = new HashMap<>();
        userPlaylistMap = new HashMap<>();
        songLikeMap = new HashMap<>();

        users = new ArrayList<>();
        songs = new ArrayList<>();
        playlists = new ArrayList<>();
        albums = new ArrayList<>();
        artists = new ArrayList<>();
    }

    public User createUser(String name, String mobile) {
        for(User u: users){
            if(u.getMobile().equals(mobile)){
                return u;
            }
        }
        User newUser= new User(name,mobile);
        users.add(newUser);
        return newUser;
    }

    public Artist createArtist(String name) {
        for(Artist a: artists){
            if(a.getName().equals(name))
                return a;
        }
        Artist artist = new Artist(name);
        artists.add(artist);
        return artist;
    }

    public Album createAlbum(String title, String artistName) {
        Artist artist= createArtist(artistName);
        for(Album album : albums){
            if(album.getTitle().equals(title))
                return  album;
        }
        Album album = new Album(title);
        albums.add(album);
        List<Album> alb = new ArrayList<>();
        if(artistAlbumMap.containsKey(artist)){
            alb=artistAlbumMap.get(artist);
        }
        alb.add(album);
        artistAlbumMap.put(artist,alb);
        return album;
    }

    public Song createSong(String title, String albumName, int length) throws Exception{
        int flag=0;
        Album album = new Album();
        for(Album CurAlbum : albums){
            if(CurAlbum.getTitle().equals(albumName)){
                album=CurAlbum;
                flag=1;
                break;
            }
        }
        if(flag==1){
            throw new Exception("Album does not exist");
        }
        Song song = new Song(title,length);
        songs.add(song);
        List<Song> songslist= new ArrayList<>();
        if(albumSongMap.containsKey(album)){
            songslist=albumSongMap.get(album);
        }
        songslist.add(song);
        albumSongMap.put(album,songslist);
        return song;
    }

    public Playlist createPlaylistOnLength(String mobile, String title, int length) throws Exception {
        for(Playlist playlist : playlists){
            if(playlist.getTitle().equals(title))
                return  playlist;
        }
        Playlist playlist = new Playlist(title);
        playlists.add(playlist);
        List<Song> temp= new ArrayList<>();
        for(Song song : songs){
            if(song.getLength()==length){
                temp.add(song);
            }
        }
        playlistSongMap.put(playlist,temp);
        User curUser= new User();
        int flag=0;
        for(User user: users){
            if(user.getMobile().equals(mobile)){
                curUser=user;
                flag= 1;
                break;
            }
        }
        if (flag==1){
            throw new Exception("User does not exist");
        }
        List<User> userslist = new ArrayList<>();
        if(playlistListenerMap.containsKey(playlist)){
            userslist=playlistListenerMap.get(playlist);
        }
        userslist.add(curUser);
        playlistListenerMap.put(playlist,userslist);

        creatorPlaylistMap.put(curUser,playlist);

        List<Playlist>userplaylists = new ArrayList<>();
        if(userPlaylistMap.containsKey(curUser)){
            userplaylists=userPlaylistMap.get(curUser);
        }
        userplaylists.add(playlist);
        userPlaylistMap.put(curUser,userplaylists);
        return playlist;
    }

    public Playlist createPlaylistOnName(String mobile, String title, List<String> songTitles) throws Exception {
        for(Playlist playlist : playlists){
            if(playlist.getTitle().equals(title))
                return  playlist;
        }
        Playlist playlist = new Playlist(title);
        playlists.add(playlist);
        List<Song> temp= new ArrayList<>();
        for(Song song : songs){
            if(songTitles.contains(song.getTitle())){
                temp.add(song);
            }
        }
        playlistSongMap.put(playlist,temp);
        User curUser= new User();
        int flag=0;
        for(User user: users){
            if(user.getMobile().equals(mobile)){
                curUser=user;
                flag= 1;
                break;
            }
        }
        if (flag==1){
            throw new Exception("User does not exist");
        }
        List<User> userslist = new ArrayList<>();
        if(playlistListenerMap.containsKey(playlist)){
            userslist=playlistListenerMap.get(playlist);
        }
        userslist.add(curUser);
        playlistListenerMap.put(playlist,userslist);

        creatorPlaylistMap.put(curUser,playlist);

        List<Playlist>userplaylists = new ArrayList<>();
        if(userPlaylistMap.containsKey(curUser)){
            userplaylists=userPlaylistMap.get(curUser);
        }
        userplaylists.add(playlist);
        userPlaylistMap.put(curUser,userplaylists);
        return playlist;
    }

    public Playlist findPlaylist(String mobile, String playlistTitle) throws Exception {
        int flag1=0;
        Playlist playlist = new Playlist();
        for(Playlist curplaylist: playlists){
            if(curplaylist.getTitle().equals(playlistTitle)){
                playlist=curplaylist;
                flag1=1;
                break;
            }
        }
        if (flag1==1){
            throw new Exception("Playlist does not exist");
        }
        User curUser= new User();
        int flag=0;
        for(User user: users){
            if(user.getMobile().equals(mobile)){
                curUser=user;
                flag= 1;
                break;
            }
        }
        if (flag==1){
            throw new Exception("User does not exist");
        }
        List<User> userslist = new ArrayList<>();
        if(playlistListenerMap.containsKey(playlist)){
            userslist=playlistListenerMap.get(playlist);
        }
        if(!userslist.contains(curUser))
            userslist.add(curUser);
        playlistListenerMap.put(playlist,userslist);
        if(creatorPlaylistMap.get(curUser)!=playlist)
            creatorPlaylistMap.put(curUser,playlist);
        List<Playlist>userplaylists = new ArrayList<>();
        if(userPlaylistMap.containsKey(curUser)){
            userplaylists=userPlaylistMap.get(curUser);
        }
        if(!userplaylists.contains(playlist))userplaylists.add(playlist);
        userPlaylistMap.put(curUser,userplaylists);
        return playlist;
    }

    public Song likeSong(String mobile, String songTitle) throws Exception {
        User curUser= new User();
        int flag=0;
        for(User user: users){
            if(user.getMobile().equals(mobile)){
                curUser=user;
                flag= 1;
                break;
            }
        }
        if (flag==1){
            throw new Exception("User does not exist");
        }
        Song song = new Song();
        int flag1=0;
        for(Song cursong : songs){
            if(cursong.getTitle().equals(songTitle)){
                song=cursong;
                flag=1;
                break;
            }
        }
        if (flag1==1){
            throw new Exception("Song does not exist");
        }
        List<User> users = new ArrayList<>();
        if(songLikeMap.containsKey(song)){
            users=songLikeMap.get(song);
        }
        if (!users.contains(curUser)){
            users.add(curUser);
            songLikeMap.put(song,users);
            song.setLikes(song.getLikes()+1);
            Album album = new Album();
            for(Album curAlbum : albumSongMap.keySet()){
                List<Song> temp = albumSongMap.get(curAlbum);
                if(temp.contains(song)){
                    album=curAlbum;
                    break;
                }
            }
            Artist artist = new Artist();
            for(Artist curArtist : artistAlbumMap.keySet()){
                List<Album> temp = artistAlbumMap.get(curArtist);
                if(temp.contains(album)){
                    artist=curArtist;
                    break;
                }
            }
            artist.setLikes(artist.getLikes()+1);
        }
        return song;
    }

    public String mostPopularArtist() {
        String name="";
        int max = Integer.MIN_VALUE;
        for(Artist art : artists){
            max= Math.max(max,art.getLikes());
        }
        for(Artist art : artists){
            if(max==art.getLikes()){
                name=art.getName();
            }
        }
        return name;
    }

    public String mostPopularSong() {
        String name="";
        int max = Integer.MIN_VALUE;
        for(Song song : songs){
            max=Math.max(max,song.getLikes());
        }
        for(Song song : songs){
            if(max==song.getLikes())
                name=song.getTitle();
        }
        return name;
    }
}