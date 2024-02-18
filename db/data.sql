insert into users (username, password, first_name, last_name, email)
values ('pesho', '1234', 'Pesho', 'Peshov', 'pesho@peshomail.com');

insert into users (username, password, first_name, last_name, email)
values ('gosho', '4321', 'Gosho', 'Goshov', 'gosho@goshomail.com');

insert into users (username, password, first_name, last_name, email)
values ('ivan', '4321', 'Ivan', 'Ivanov', 'ivam@ivanmail.com');

INSERT INTO forum.posts (title, content, likes, dislikes, created_by, archived, stamp_created) VALUES ('Calling All Car Enthusiasts! Let\'s Talk Winter Maintenance', 'Hey fellow gearheads,

As the temperature drops and winter sets in, it\'s time to start thinking about how to keep our vehicles running smoothly in the cold weather. From checking tire pressure to topping up windshield washer fluid, there are a lot of things to consider when it comes to winter maintenance.

I\'ve compiled some tips and tricks to help you winterize your ride and ensure it\'s ready to tackle whatever Mother Nature throws our way. So grab a cup of hot cocoa, pull up a chair, and let\'s chat about all things winter car care!

Looking forward to hearing your thoughts and swapping stories about winter driving adventures. Stay safe out there on the snowy roads, everyone!', 2, 1, 1, 0, '2024-02-14 12:46:03');
INSERT INTO forum.posts (title, content, likes, dislikes, created_by, archived, stamp_created) VALUES ('Electric vs. Gas: The Great Debate', 'Hey car aficionados,

Are you torn between going electric or sticking with a traditional gasoline-powered vehicle? You\'re not alone! The debate between electric and gas cars is hotter than ever, and there\'s a lot to consider when making the switch.

From environmental impact to driving range, there are pros and cons to both options. But fear not – I\'ve done some research and put together a breakdown of the key differences between electric and gas vehicles to help you make an informed decision.

So if you\'re on the fence about which type of car to invest in, join the discussion and let\'s weigh the pros and cons together. Who knows, maybe we\'ll all learn something new in the process!', 0, 0, 2, 0, '2024-02-14 12:46:03');
INSERT INTO forum.posts (title, content, likes, dislikes, created_by, archived, stamp_created) VALUES ('Classic Cars: A Blast from the Past', 'Hey there fellow petrolheads,

There\'s something undeniably special about classic cars, don\'t you think? Whether it\'s the sleek curves of a vintage sports car or the rugged charm of a muscle car from the \'60s, classic rides have a way of capturing our hearts and imaginations.

In this post, I want to celebrate the timeless beauty and nostalgia of classic cars. Let\'s reminisce about our favorite models, share restoration stories, and maybe even drool over some stunning photos of vintage rides.

So if you\'re a sucker for classic cars like I am, pull up a seat and join the conversation. I can\'t wait to hear about your favorite classics and swap stories about the ones that got away!', 0, 0, 1, 0, '2024-02-14 12:46:03');
INSERT INTO forum.posts (title, content, likes, dislikes, created_by, archived, stamp_created) VALUES ('Road Trip Tips: Adventures Await!', 'Hey fellow road warriors,

Is there anything better than hitting the open road and embarking on an epic road trip? Whether you\'re cruising down Route 66 or exploring the winding roads of the countryside, there\'s a certain sense of freedom and excitement that comes with traveling by car.

In this post, I want to share some of my top road trip tips and tricks to help you make the most of your next adventure. From packing essentials to planning the perfect playlist, there\'s a lot to consider when it comes to road tripping like a pro.

So grab your sunglasses and fill up the tank – it\'s time to hit the road and create some unforgettable memories. I can\'t wait to hear about your favorite road trip destinations and swap stories about the highways and byways we\'ve traveled. Let the adventures begin!', 0, 0, 1, 0, '2024-02-15 16:05:12');
INSERT INTO forum.posts (title, content, likes, dislikes, created_by, archived, stamp_created) VALUES ('Ahoy, Boating Enthusiasts! Let\'s Talk About Nautical Adventures', 'Hey there, fellow sailors and boating enthusiasts!

There\'s something magical about being out on the water, isn\'t there? Whether you\'re cruising along the coastline, fishing in a serene lake, or sailing the open seas, boating offers endless opportunities for adventure and relaxation.

In this post, I want to dive into all things boating – from sharing tips on boat maintenance and navigation to swapping stories about our most memorable boating experiences. So grab your captain\'s hat and join me as we set sail on a virtual voyage through the wonderful world of boating.

I can\'t wait to hear about your favorite boating destinations, the biggest fish you\'ve ever caught, and the friends and family you\'ve shared your love of boating with. Fair winds and following seas, my friends!

Title: Flying High: Let\'s Discuss Al', 0, 0, 1, 0, '2024-02-15 16:05:14');
INSERT INTO forum.posts (title, content, likes, dislikes, created_by, archived, stamp_created) VALUES ('Flying High: Let\'s Discuss All Things Aviation', 'Flying isn\'t just a mode of transportation – it\'s a passion, a lifestyle, and a thrilling adventure all rolled into one. Whether you\'re a seasoned pilot, a student learning to fly, or simply fascinated by the world of aviation, there\'s something for everyone to love about taking to the skies.

In this post, I want to spark a conversation about all things aviation. From sharing our favorite aircraft and airshows to discussing the latest advancements in aviation technology, let\'s come together as a community of aviation enthusiasts to celebrate our love of flight.

So buckle up and prepare for takeoff – the sky\'s the limit when it comes to discussing our shared passion for aviation. I can\'t wait to hear about your first solo flight, your dream aircraft, and the breathtaking views you\'ve experienced from the cockpit. Happy flying, everyone!', 0, 0, 1, 0, '2024-02-15 16:05:15');
INSERT INTO forum.posts (title, content, likes, dislikes, created_by, archived, stamp_created) VALUES ('Snowmobile Adventures: Let\'s Ride into Winter Wonderland', 'Hey snowmobilers and winter sports enthusiasts!

As the temperatures drop and the snow begins to fall, it\'s time to break out our snowmobiles and embark on some adrenaline-fueled adventures in the great outdoors. Whether you\'re tearing up the trails, carving through fresh powder, or racing across frozen lakes, snowmobiling offers thrills and excitement like no other winter sport.

In this post, I want to gather fellow snowmobile enthusiasts to share tips, tricks, and stories about our snowmobiling adventures. From choosing the right sled and maintaining it for peak performance to finding the best trails and destinations, there\'s a lot to discuss when it comes to snowmobiling.

So bundle up, fire up your sled, and let\'s hit the trails together. I can\'t wait to hear about your most epic snowmobiling adventures, the craziest stunts you\'ve pulled off, and the breathtaking winter landscapes you\'ve explored from the back of your sled. Let the snowmobiling season begin!

Feel free to share your own experiences, ask questions, or simply join in on the conversation. After all, the more, the merrier when it comes to discussing our shared passion for snowmobiling. Let\'s ride!', 0, 0, 1, 0, '2024-02-15 16:05:16');


insert into likes_dislikes (post_id, user_id, like_dislike)
values (1, 1, 'LIKE');

insert into likes_dislikes (post_id, user_id, like_dislike)
values (1, 2, 'LIKE');

insert into likes_dislikes (post_id, user_id, like_dislike)
values (1, 3, 'DISLIKE');

INSERT INTO comments (content, user_id, post_id)
VALUES ('test', 1, 1);
INSERT INTO comments (content, user_id, post_id)
VALUES ('comment2', 2, 1);
INSERT INTO comments (content, user_id, post_id)
VALUES ('comment3', 3, 3);


INSERT INTO forum.tags (name) VALUES ('bikes');
INSERT INTO forum.tags (name) VALUES ('cars');
INSERT INTO forum.tags (name) VALUES ('snowmobiles');
INSERT INTO forum.tags (name) VALUES ('spacecrafts');
INSERT INTO forum.tags (name) VALUES ('testing');

INSERT INTO forum.posts_tags (post_id, tag_id)
VALUES (1, 1);
INSERT INTO forum.posts_tags (post_id, tag_id)
VALUES (2, 2);
INSERT INTO forum.posts_tags (post_id, tag_id)
VALUES (3, 1);

INSERT INTO roles (role)
VALUES ('ADMIN');
INSERT INTO roles (role)
VALUES ('USER');

INSERT INTO users_roles (user_id, role_id)
VALUES (1, 1);
INSERT INTO users_roles (user_id, role_id)
VALUES (1, 2);
INSERT INTO users_roles (user_id, role_id)
VALUES (2, 2);
INSERT INTO users_roles (user_id, role_id)
VALUES (3, 2);