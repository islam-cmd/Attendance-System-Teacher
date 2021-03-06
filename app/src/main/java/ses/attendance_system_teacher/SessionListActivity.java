package ses.attendance_system_teacher;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.util.ArrayList;

import ses.attendance_system_teacher.model.Session;

public class SessionListActivity extends AppCompatActivity {

    FloatingActionButton btn_add_session;
    RecyclerView rv_sessions;
    ArrayList<Session> session_list;
    SessionRCAdapter sessionRCAdapter;
    FirebaseUser firebaseUser;
    DatabaseReference sessionDatabaseReference;
    DatabaseReference teacherSubjectsDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_sessions_list);
        //toolBarLayout.setTitle(getTitle());
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        sessionDatabaseReference = FirebaseDatabase.getInstance().getReference("Session");
        teacherSubjectsDatabaseReference = FirebaseDatabase.getInstance().getReference("Teacher").child(firebaseUser.getUid()).child("teacher_subjects");
        btn_add_session = findViewById(R.id.btn_add_session);
        rv_sessions = findViewById(R.id.rv_sessions);
        session_list = new ArrayList<>();

        btn_add_session.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();
                startActivity(new Intent(SessionListActivity.this, AddSessionActivity.class));
            }
        });
        sessionRCAdapter = new SessionRCAdapter(session_list);
        teacherSubjectsDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot subjectSnapshot: snapshot.getChildren()) {
                    sessionDatabaseReference.child(subjectSnapshot.getKey()).getRef().addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                            Session session = snapshot.getValue(Session.class);
                            session_list.add(session);
                            sessionRCAdapter.notifyItemInserted(session_list.size());
                            Log.v("Session", "loaded");
                        }

                        @Override
                        public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                        }

                        @Override
                        public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                        }

                        @Override
                        public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        rv_sessions.setLayoutManager(new LinearLayoutManager(this));
        rv_sessions.setAdapter(sessionRCAdapter);

    }
}