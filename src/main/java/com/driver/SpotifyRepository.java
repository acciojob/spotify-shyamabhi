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
        for(User u : users){
            if(u.getMobile().equals(mobile)){
                return u;
            }
        }
        User user = new User(name,mobile);
        users.add(user);
        return user;
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
        Artist artist = createArtist(artistName);

        for(Album a : albums){
            if(a.getTitle().equals(title)){
                return a;
            }
        }
        Album album = new Album(title);
        albums.add(album);

        List<Album>l = new ArrayList<>();
        if(artistAlbumMap.containsKey(artist)){
            l = artistAlbumMap.get(artist);
        }
        l.add(album);
        artistAlbumMap.put(artist,l);
        return album;
    }

    public Song createSong(String title, String albumName, int length) throws Exception{
        int flag=0;
        Album album = new Album();
        for(Album a : albums){
            if(a.getTitle().equals(albumName)){
                flag=1;
                album = a;
                break;
            }
        }
        if(flag == 1){
            throw new Exception("Album does not exist");
        }
        Song song = new Song(title,length);
        songs.add(song);

        List<Song>l = new ArrayList<>();
        if(albumSongMap.containsKey(albumName)){
           l = albumSongMap.get(albumName);
        }
        l.add(song);
        albumSongMap.put(album,l);
        return song;
    }

    public Playlist createPlaylistOnLength(String mobile, String title, int length) throws Exception {
        for(Playlist p : playlists){
            if(p.getTitle().equals(title)){
                return p;
            }
        }
        Playlist playlist = new Playlist(title);
        playlists.add(playlist);
        List<Song>l=new ArrayList<>();
        for(Song s : songs){
            if(s.getLength() == length){
                l.add(s);
            }
        }
        playlistSongMap.put(playlist,l);
        User user = same(mobile);
        List<User> userList = new ArrayList<>();
        if(playlistListenerMap.containsKey(playlist)){
            userList = playlistListenerMap.get(playlist);
        }
        userList.add(user);
        playlistListenerMap.put(playlist,userList);
        creatorPlaylistMap.put(user,playlist);

        List<Playlist>playlistList = new ArrayList<>();
        if (userPlaylistMap.containsKey(user)){
            playlistList = userPlaylistMap.get(user);
        }
        playlistList.add(playlist);
        userPlaylistMap.put(user,playlistList);
        return playlist;

    }

    public Playlist createPlaylistOnName(String mobile, String title, List<String> songTitles) throws Exception {
        for(Playlist playlist : playlists){
            if(playlist.getTitle().equals(title))
                return  playlist;
        }
        Playlist playlist = new Playlist(title);
        // adding playlist to playlists list
        playlists.add(playlist);

        List<Song> temp= new ArrayList<>();
        for(Song song : songs){
            if(songTitles.contains(song.getTitle())){
                temp.add(song);
            }
        }
        playlistSongMap.put(playlist,temp);

        User user = same(mobile);

        List<User> userslist = new ArrayList<>();
        if(playlistListenerMap.containsKey(playlist)){
            userslist=playlistListenerMap.get(playlist);
        }
        userslist.add(user);
        playlistListenerMap.put(playlist,userslist);

        creatorPlaylistMap.put(user,playlist);

        List<Playlist>userplaylists = new ArrayList<>();
        if(userPlaylistMap.containsKey(user)){
            userplaylists=userPlaylistMap.get(user);
        }
        userplaylists.add(playlist);
        userPlaylistMap.put(user,userplaylists);

        return playlist;

    }

    public Playlist findPlaylist(String mobile, String playlistTitle) throws Exception {
        int flag=0;
        Playlist playlist = new Playlist();
        for(Playlist p: playlists){
            if(p.getTitle().equals(playlistTitle)){
                playlist=p;
                flag=1;
                break;
            }
        }
        if (flag==1){
            throw new Exception("Playlist does not exist");
        }
        User user = same(mobile);
        List<User> userlist = new ArrayList<>();
        if(playlistListenerMap.containsKey(playlist)){
            userlist=playlistListenerMap.get(playlist);
        }
        if(!userlist.contains(user)){
            userlist.add(user);
        }
        playlistListenerMap.put(playlist,userlist);

        if(creatorPlaylistMap.get(user)!=playlist){
            creatorPlaylistMap.put(user,playlist);
        }
        List<Playlist>userplaylists = new ArrayList<>();
        if(userPlaylistMap.containsKey(user)){
            userplaylists=userPlaylistMap.get(user);
        }
        if(!userplaylists.contains(playlist))userplaylists.add(playlist);

        userPlaylistMap.put(user,userplaylists);
        return playlist;
    }

    public Song likeSong(String mobile, String songTitle) throws Exception {
        User curUser= new User();
        boolean flag2= false;
        for(User user: users){
            if(user.getMobile().equals(mobile)){
                curUser=user;
                flag2= true;
                break;
            }
        }
        if (flag2==false){
            throw new Exception("User does not exist");
        }

        Song song = new Song();
        boolean flag = false;
        for(Song cursong : songs){
            if(cursong.getTitle().equals(songTitle)){
                song=cursong;
                flag=true;
                break;
            }
        }
        if (flag==false){
            throw new Exception("Song does not exist");
        }

        //public HashMap<Song, List<User>> songLikeMap;
        List<User> users = new ArrayList<>();
        if(songLikeMap.containsKey(song)){
            users=songLikeMap.get(song);
        }
        if (!users.contains(curUser)){
            users.add(curUser);
            songLikeMap.put(song,users);
            song.setLikes(song.getLikes()+1);


//            public HashMap<Album, List<Song>> albumSongMap;
            Album album = new Album();
            for(Album curAlbum : albumSongMap.keySet()){
                List<Song> temp = albumSongMap.get(curAlbum);
                if(temp.contains(song)){
                    album=curAlbum;
                    break;
                }
            }


//            public HashMap<Artist, List<Album>> artistAlbumMap;
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
        int maxLikes = Integer.MIN_VALUE;
        for(Artist art : artists){
            maxLikes= Math.max(maxLikes,art.getLikes());
        }
        for(Artist art : artists){
            if(maxLikes==art.getLikes()){
                name=art.getName();
            }
        }
        return name;
    }

    public String mostPopularSong() {
        String name="";
        int maxLikes = Integer.MIN_VALUE;
        for(Song song : songs){
            maxLikes=Math.max(maxLikes,song.getLikes());
        }
        for(Song song : songs){
            if(maxLikes==song.getLikes())
                name=song.getTitle();
        }
        return name;
    }









    public User same(String mobile) throws Exception {
        int flag=0;
        User user = new User();
        for(User u : users){
            if(u.getMobile().equals(mobile)){
                flag=1;
                user = u;
                break;
            }
        }
        if(flag==1){
            throw new Exception("User does not exist");
        }
        return user;
    }
}
