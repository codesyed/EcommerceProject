package com.socialmedia.app;

import com.socialmedia.app.model.SocialGroup;
import com.socialmedia.app.model.SocialPost;
import com.socialmedia.app.model.SocialProfile;
import com.socialmedia.app.model.SocialUser;
import com.socialmedia.app.repository.GroupRepo;
import com.socialmedia.app.repository.PostRepo;
import com.socialmedia.app.repository.ProfileRepo;
import com.socialmedia.app.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Autowired
    UserRepo userRepository;
    @Autowired
    ProfileRepo socialProfileRepository;
    @Autowired
    PostRepo postRepository;
    @Autowired
    GroupRepo groupRepository;

    @Bean
    public CommandLineRunner initializeData() {
        return args -> {

            // Create some users
            SocialUser user1 = new SocialUser();  user1.setUserName("Syed");
            SocialUser user2 = new SocialUser();  user2.setUserName("Abdur Khan");
            SocialUser user3 = new SocialUser();  user3.setUserName("Adil");

            // Save users to the database
            userRepository.save(user1);
            userRepository.save(user2);
            userRepository.save(user3);

            // Create some groups
            SocialGroup group1 = new SocialGroup();  group1.setGroupHeading("Group-1");
            SocialGroup group2 = new SocialGroup();  group2.setGroupHeading("Group-2");

            /* In group and User Relation (ManyToMany) - Owner side User*/
            // Add users to groups
            group1.getUsers().add(user1);
            group1.getUsers().add(user2);

            group2.getUsers().add(user2);
            group2.getUsers().add(user3);

            // Save groups to the database
            groupRepository.save(group1);
            groupRepository.save(group2);

            user1.getUserSocialGroups().add(group1);
            user2.getUserSocialGroups().add(group1);
            user2.getUserSocialGroups().add(group2);
            user3.getUserSocialGroups().add(group2);

            // Save users to the database to update associtions
            userRepository.save(user1);
            userRepository.save(user2);
            userRepository.save(user3);



            //Forget putting group inside user (this is imp Owning condition)

            //Post-User Post is Owning side
            // Create some posts
            SocialPost post1 = new SocialPost();
            SocialPost post2 = new SocialPost();
            SocialPost post3 = new SocialPost();

            // Associate posts with users
            post1.setSocialUser(user1);
            post2.setSocialUser(user2);
            post3.setSocialUser(user3);

            // Save posts to the db(assuming we have a PostRepository)
            postRepository.save(post1);
            postRepository.save(post2);
            postRepository.save(post3);

            //Profile-User Profile is owner side
            // Create some social profiles
            SocialProfile profile1 = new SocialProfile();
            SocialProfile profile2 = new SocialProfile();
            SocialProfile profile3 = new SocialProfile();

            // Associate profiles with users
            profile1.setSocialUser(user1);
            profile2.setSocialUser(user2);
            profile3.setSocialUser(user3);

            // Save profiles
            socialProfileRepository.save(profile1);
            socialProfileRepository.save(profile2);
            socialProfileRepository.save(profile3);
        };
    }
}
