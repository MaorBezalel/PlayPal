package com.hit.playpal.chatrooms.ui.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.hit.playpal.R;
import com.hit.playpal.entities.chats.OneToOneChatRoom;
import com.hit.playpal.entities.users.User;

import java.util.ArrayList;
import java.util.HashMap;

public class TestChatRoomActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_test_chat_room);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        // --------------------------------------TEST-----------------------------------------------
        // TestChatRoomActivity is a test class that is used to test the ChatRoomActivity class.

        String image1 = "https://hips.hearstapps.com/hmg-prod/images/beautiful-smooth-haired-red-cat-lies-on-the-sofa-royalty-free-image-1678488026.jpg";
        User user1 = new User(
                "IO6HNmPe34g1qFdIZXUbyR0zreR2",
                "maorb",
                "⚔️MaorB⚔️",
                image1,
                "I'm a software engineer and a cat lover 🐱"
        );

        String image2 = "https://cdn-prod.medicalnewstoday.com/content/images/articles/322/322868/golden-retriever-puppy.jpg";
        User user2 = new User(
                "mjapeA2vPFadWHPdpdkxgvJ6Te02",
                "dog_lover",
                "⚔️🐶⚔️",
                image2,
                "I'm a dog lover 🐶"
        );

        OneToOneChatRoom chatRoom = new OneToOneChatRoom(
                "ooeUKMSDITrM1WnOHYKc",
                null,
                new HashMap<String, OneToOneChatRoom.OtherMemberData>() {{
                    put(user1.getUid(), new OneToOneChatRoom.OtherMemberData(user2.getDisplayName(), user2.getProfilePicture()));
                    put(user2.getUid(), new OneToOneChatRoom.OtherMemberData(user1.getDisplayName(), user1.getProfilePicture()));
                }}
        );

        Intent intent = new Intent(this, ChatRoomActivity.class);

        intent.putExtra("user", user1); // or user2 depending on the current user
        intent.putExtra("chatRoom", chatRoom);

        startActivity(intent);
        finish();
        // -----------------------------------------------------------------------------------------
    }
}