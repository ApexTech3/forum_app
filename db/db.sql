drop schema if exists `forum`;

create schema `forum`;

use `forum`;

create table `users`
(
    `user_id`    int(11) auto_increment primary key,
    `username`   varchar(30) not null,
    `password`   varchar(30) not null,
    `first_name` varchar(32) not null,
    `last_name`  varchar(32) not null,
    `email`      varchar(50) not null,
    `is_blocked` tinyint(1)           default 0 not null,
    `is_deleted` tinyint(1)  NOT NULL DEFAULT 0,
    UNIQUE KEY `users_pk` (`username`),
    UNIQUE KEY `users_pk2` (`email`)
);

create table `posts`
(
    `post_id`    int(11) auto_increment primary key,
    `title`      varchar(64)          not null,
    `content`    varchar(8192)        not null,
    `likes`      int(11)    default 0 not null,
    `dislikes`   int(11)    default 0 not null,
    `created_by` int(11)              not null,
    `archived`   tinyint(1) default 0 not null,
    stamp_created timestamp default current_timestamp,
    constraint posts_users_user_id_fk_2

        foreign key (created_by) references users (user_id)
);


create table likes_dislikes
(
    like_id      int auto_increment
        primary key,
    post_id      int                      not null,
    user_id      int                      not null,
    like_dislike enum ('LIKE', 'DISLIKE') not null,
    constraint likes_posts_post_id_fk
        foreign key (post_id) references posts (post_id),
    constraint likes_users_user_id_fk
        foreign key (user_id) references users (user_id),
    constraint uc_post_id_user_id
        unique (post_id, user_id)
);
CREATE TABLE `phones`
(
    `phone_id` int(11)     NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `user_id`  int(11)     NOT NULL,
    `phone`    varchar(50) NOT NULL,
    CONSTRAINT `phones_users_user_id_fk` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
);

CREATE TABLE `pictures`
(
    `picture_id` int(11)     NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `user_id`    int(11)     NOT NULL,
    `picture`    varchar(256) NOT NULL,
    CONSTRAINT `pictures_users_user_id_fk` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
);

create table tags
(
    tag_id int auto_increment
        primary key,
    name   varchar(255) not null,
    constraint name
        unique (name)
);

create table posts_tags
(
    post_id int not null,
    tag_id  int not null,
    primary key (post_id, tag_id),
    constraint post_tag_ibfk_1
        foreign key (post_id) references posts (post_id),
    constraint post_tag_ibfk_2
        foreign key (tag_id) references tags (tag_id)
);

CREATE TABLE `roles`
(
    `role_id` int(11)     NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `role`    varchar(20) NOT NULL
);

create table comments
(
    comment_id    int auto_increment
        primary key,
    content       text                                  not null,
    user_id       int                                   null,
    post_id       int                                   null,
    stamp_created timestamp default current_timestamp() not null,
    constraint FK_Comment_Post
        foreign key (post_id) references posts (post_id),
    constraint FK_Comment_User
        foreign key (user_id) references users (user_id)
)
    collate = utf8mb4_unicode_ci;

CREATE TABLE `users_roles`
(
    `users_roles_id` int(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `user_id`        int(11) NOT NULL,
    `role_id`        int(11) NOT NULL,
    CONSTRAINT `users_roles_roles_role_id_fk` FOREIGN KEY (`role_id`) REFERENCES `roles` (`role_id`),
    CONSTRAINT `users_roles_users_user_id_fk` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
);

create index tag_id
    on posts_tags (tag_id);


DELIMITER //
CREATE TRIGGER update_likes_dislikes
    AFTER INSERT
    ON likes_dislikes
    FOR EACH ROW
BEGIN
    DECLARE post_like INT;
    DECLARE post_dislike INT;

    -- Get the current like and dislike counts for the post
    SELECT likes, dislikes
    INTO post_like, post_dislike
    FROM posts
    WHERE post_id = NEW.post_id;

    -- Check if it's a like or dislike and update the appropriate counter
    IF NEW.like_dislike = 'LIKE' THEN
        -- It's a like
        UPDATE posts
        SET likes = post_like + 1
        WHERE post_id = NEW.post_id;
    ELSE
        -- It's a dislike
        UPDATE posts
        SET dislikes = post_dislike + 1
        WHERE post_id = NEW.post_id;
    END IF;
END;
//

CREATE TRIGGER update_likes_dislikes_after_update
    AFTER UPDATE
    ON likes_dislikes
    FOR EACH ROW
BEGIN
    DECLARE post_like INT;
    DECLARE post_dislike INT;

    -- Get the current like and dislike counts for the post
    SELECT likes, dislikes
    INTO post_like, post_dislike
    FROM posts
    WHERE post_id = NEW.post_id;

    -- Check if it's a like or dislike and update the appropriate counters
    IF NEW.like_dislike = 'LIKE' THEN
        -- It's a like
        UPDATE posts
        SET likes    = post_like + 1,
            dislikes = post_dislike - 1
        WHERE post_id = NEW.post_id;
    ELSE
        -- It's a dislike
        UPDATE posts
        SET dislikes = post_dislike + 1,
            likes    = post_like - 1
        WHERE post_id = NEW.post_id;
    END IF;
END;
//
DELIMITER ;

insert into users (username, password, first_name, last_name, email) values ('pesho', '1234', 'Pesho', 'Peshov', 'pesho@peshomail.com');
insert into users (username, password, first_name, last_name, email) values ('gosho', '1234', 'Gosho', 'Goshov', 'gosho@goshomail.com');
insert into users (username, password, first_name, last_name, email) values ('tosho', '1234', 'Tosho', 'Toshov', 'tosho@toshomail.com');
insert into users (username, password, first_name, last_name, email) values ('ivan', '1234', 'Ivan', 'Ivanov', 'ivan@ivanmail.com');
insert into users (username, password, first_name, last_name, email) values ('mariika', '1234', 'Mariika', 'Mariikova', 'mariika@mariikamail.com');
insert into users (username, password, first_name, last_name, email) values ('sasho', '1234', 'Sasho', 'Sashov', 'sasho@sashomail.com');
insert into users (username, password, first_name, last_name, email) values ('marto', '1234', 'Marto', 'Martov', 'marto@martomail.com');
insert into users (username, password, first_name, last_name, email) values ('bobi', '1234', 'Bobi', 'Bobov', 'bobi@bobimail.com');
insert into users (username, password, first_name, last_name, email) values ('niki', '1234', 'Niki', 'Nikov', 'niki@nikimail.com');
insert into users (username, password, first_name, last_name, email) values ('geri', '1234', 'Geri', 'Gerova', 'geri@gerimail.com');
insert into users (username, password, first_name, last_name, email) values ('nscoterbosh0', 'qE3#vp#1', 'Nannette', 'Scoterbosh', 'nscoterbosh0@canalblog.com');
insert into users (username, password, first_name, last_name, email) values ('rchicken1', 'bQ3&9)c0Ts', 'Rockie', 'Chicken', 'rchicken1@ucoz.ru');
insert into users (username, password, first_name, last_name, email) values ('egeorgel2', 'vK4(?$K)sxiBZ', 'Eleanor', 'Georgel', 'egeorgel2@surveymonkey.com');
insert into users (username, password, first_name, last_name, email) values ('possulton3', 'tR2x/oD!''/Lm3T', 'Parsifal', 'Ossulton', 'possulton3@sina.com.cn');
insert into users (username, password, first_name, last_name, email) values ('sharrema4', 'sS6)twlL#c(&', 'Saunder', 'Harrema', 'sharrema4@google.co.uk');
insert into users (username, password, first_name, last_name, email) values ('ikobes5', 'bB5,(")QSQF', 'Inglis', 'Kobes', 'ikobes5@friendfeed.com');
insert into users (username, password, first_name, last_name, email) values ('ideclercq6', 'iQ0(?qQ+BB@ex', 'Ingeberg', 'de Clercq', 'ideclercq6@amazon.com');
insert into users (username, password, first_name, last_name, email) values ('tipplett7', 'eT2*kk4r9p6w=0N7', 'Taryn', 'Ipplett', 'tipplett7@walmart.com');
insert into users (username, password, first_name, last_name, email) values ('kkearley8', 'rX4,VV7Q=A(3c', 'Kriste', 'Kearley', 'kkearley8@dyndns.org');
insert into users (username, password, first_name, last_name, email) values ('dfawdrey9', 'iJ6''TpL,udK}ct', 'Delinda', 'Fawdrey', 'dfawdrey9@baidu.com');
insert into users (username, password, first_name, last_name, email) values ('bdrillingcourta', 'tP6*dcaJ5<', 'Bernette', 'Drillingcourt', 'bdrillingcourta@chron.com');
insert into users (username, password, first_name, last_name, email) values ('mcaenb', 'yF6,_O''xY@Oh', 'Maddy', 'Caen', 'mcaenb@samsung.com');
insert into users (username, password, first_name, last_name, email) values ('mrootec', 'iM2!v9/XN8q', 'Melissa', 'Roote', 'mrootec@google.com.hk');
insert into users (username, password, first_name, last_name, email) values ('nbanatd', 'oS4|_uDeBZCy400M', 'Norry', 'Banat', 'nbanatd@accuweather.com');
insert into users (username, password, first_name, last_name, email) values ('aacome', 'uW1"u)w#!j', 'Adela', 'Acom', 'aacome@oaic.gov.au');
insert into users (username, password, first_name, last_name, email) values ('lgatchelf', 'qI9,c+&*P%Ep.Pl', 'Lelah', 'Gatchel', 'lgatchelf@pinterest.com');
insert into users (username, password, first_name, last_name, email) values ('dcornelisseng', 'iM2''M(v&{gBPtE', 'Dulcea', 'Cornelissen', 'dcornelisseng@pen.io');
insert into users (username, password, first_name, last_name, email) values ('sstalmanh', 'bC9_I%niA"', 'Sheffy', 'Stalman', 'sstalmanh@guardian.co.uk');
insert into users (username, password, first_name, last_name, email) values ('zzoepheli', 'nH0~wm7|~K&of(', 'Zed', 'Zoephel', 'zzoepheli@walmart.com');
insert into users (username, password, first_name, last_name, email) values ('jmartynikhinj', 'rF6,%q).}$qQ', 'Joelynn', 'Martynikhin', 'jmartynikhinj@google.cn');

INSERT INTO posts (title, content, likes, dislikes, created_by, archived, stamp_created)
VALUES ('Calling All Car Enthusiasts! Let\'s Talk Winter Maintenance', 'Hey fellow gearheads,

As the temperature drops and winter sets in, it\'s time to start thinking about how to keep our vehicles running smoothly in the cold weather. From checking tire pressure to topping up windshield washer fluid, there are a lot of things to consider when it comes to winter maintenance.

I\'ve compiled some tips and tricks to help you winterize your ride and ensure it\'s ready to tackle whatever Mother Nature throws our way. So grab a cup of hot cocoa, pull up a chair, and let\'s chat about all things winter car care!

Looking forward to hearing your thoughts and swapping stories about winter driving adventures. Stay safe out there on the snowy roads, everyone!',
        2, 1, 1, 0, '2024-02-14 12:46:03');
INSERT INTO posts (title, content, likes, dislikes, created_by, archived, stamp_created)
VALUES ('Electric vs. Gas: The Great Debate', 'Hey car aficionados,

Are you torn between going electric or sticking with a traditional gasoline-powered vehicle? You\'re not alone! The debate between electric and gas cars is hotter than ever, and there\'s a lot to consider when making the switch.

From environmental impact to driving range, there are pros and cons to both options. But fear not – I\'ve done some research and put together a breakdown of the key differences between electric and gas vehicles to help you make an informed decision.

So if you\'re on the fence about which type of car to invest in, join the discussion and let\'s weigh the pros and cons together. Who knows, maybe we\'ll all learn something new in the process!',
        0, 0, 2, 0, '2024-02-14 12:46:03');
INSERT INTO posts (title, content, likes, dislikes, created_by, archived, stamp_created)
VALUES ('Classic Cars: A Blast from the Past', 'Hey there fellow petrolheads,

There\'s something undeniably special about classic cars, don\'t you think? Whether it\'s the sleek curves of a vintage sports car or the rugged charm of a muscle car from the \'60s, classic rides have a way of capturing our hearts and imaginations.

In this post, I want to celebrate the timeless beauty and nostalgia of classic cars. Let\'s reminisce about our favorite models, share restoration stories, and maybe even drool over some stunning photos of vintage rides.

So if you\'re a sucker for classic cars like I am, pull up a seat and join the conversation. I can\'t wait to hear about your favorite classics and swap stories about the ones that got away!',
        0, 0, 1, 0, '2024-02-14 12:46:03');
INSERT INTO posts (title, content, likes, dislikes, created_by, archived, stamp_created)
VALUES ('Road Trip Tips: Adventures Await!', 'Hey fellow road warriors,

Is there anything better than hitting the open road and embarking on an epic road trip? Whether you\'re cruising down Route 66 or exploring the winding roads of the countryside, there\'s a certain sense of freedom and excitement that comes with traveling by car.

In this post, I want to share some of my top road trip tips and tricks to help you make the most of your next adventure. From packing essentials to planning the perfect playlist, there\'s a lot to consider when it comes to road tripping like a pro.

So grab your sunglasses and fill up the tank – it\'s time to hit the road and create some unforgettable memories. I can\'t wait to hear about your favorite road trip destinations and swap stories about the highways and byways we\'ve traveled. Let the adventures begin!',
        0, 0, 1, 0, '2024-02-15 16:05:12');
INSERT INTO posts (title, content, likes, dislikes, created_by, archived, stamp_created)
VALUES ('Ahoy, Boating Enthusiasts! Let\'s Talk About Nautical Adventures', 'Hey there, fellow sailors and boating enthusiasts!

There\'s something magical about being out on the water, isn\'t there? Whether you\'re cruising along the coastline, fishing in a serene lake, or sailing the open seas, boating offers endless opportunities for adventure and relaxation.

In this post, I want to dive into all things boating – from sharing tips on boat maintenance and navigation to swapping stories about our most memorable boating experiences. So grab your captain\'s hat and join me as we set sail on a virtual voyage through the wonderful world of boating.

I can\'t wait to hear about your favorite boating destinations, the biggest fish you\'ve ever caught, and the friends and family you\'ve shared your love of boating with. Fair winds and following seas, my friends!

Title: Flying High: Let\'s Discuss Al', 0, 0, 1, 0, '2024-02-15 16:05:14');
INSERT INTO posts (title, content, likes, dislikes, created_by, archived, stamp_created)
VALUES ('Flying High: Let\'s Discuss All Things Aviation', 'Flying isn\'t just a mode of transportation – it\'s a passion, a lifestyle, and a thrilling adventure all rolled into one. Whether you\'re a seasoned pilot, a student learning to fly, or simply fascinated by the world of aviation, there\'s something for everyone to love about taking to the skies.

In this post, I want to spark a conversation about all things aviation. From sharing our favorite aircraft and airshows to discussing the latest advancements in aviation technology, let\'s come together as a community of aviation enthusiasts to celebrate our love of flight.

So buckle up and prepare for takeoff – the sky\'s the limit when it comes to discussing our shared passion for aviation. I can\'t wait to hear about your first solo flight, your dream aircraft, and the breathtaking views you\'ve experienced from the cockpit. Happy flying, everyone!',
        0, 0, 1, 0, '2024-02-15 16:05:15');
INSERT INTO posts (title, content, likes, dislikes, created_by, archived, stamp_created)
VALUES ('Snowmobile Adventures: Let\'s Ride into Winter Wonderland', 'Hey snowmobilers and winter sports enthusiasts!

As the temperatures drop and the snow begins to fall, it\'s time to break out our snowmobiles and embark on some adrenaline-fueled adventures in the great outdoors. Whether you\'re tearing up the trails, carving through fresh powder, or racing across frozen lakes, snowmobiling offers thrills and excitement like no other winter sport.

In this post, I want to gather fellow snowmobile enthusiasts to share tips, tricks, and stories about our snowmobiling adventures. From choosing the right sled and maintaining it for peak performance to finding the best trails and destinations, there\'s a lot to discuss when it comes to snowmobiling.

So bundle up, fire up your sled, and let\'s hit the trails together. I can\'t wait to hear about your most epic snowmobiling adventures, the craziest stunts you\'ve pulled off, and the breathtaking winter landscapes you\'ve explored from the back of your sled. Let the snowmobiling season begin!

Feel free to share your own experiences, ask questions, or simply join in on the conversation. After all, the more, the merrier when it comes to discussing our shared passion for snowmobiling. Let\'s ride!',
        0, 0, 1, 0, '2024-02-15 16:05:16');
insert into posts (title, content, created_by, stamp_created) values ('Revolutionizing Automobiles: The Rise of Electric Vehicles', 'In recent times, the automotive landscape has experienced a profound transformation, propelled by the burgeoning adoption of electric vehicles (EVs). This transformative wave represents a crucial junctureactive option for a wide spectrum of drivers. This accelerating trend underscores a fundamental reimagining of transportation, paving the way for a cleaner, greener, and more sustainable future on our roads.', 5, '2011-02-20 03:50:51');
insert into posts (title, content, created_by, stamp_created) values ('The Future of Mobility: Exploring Autonomous Vehicles', 'In envisioning the future of transportation, autonomous vehicles stand at the forefront of innovation, poised to revolutionize the way we commute and travel. The advent of self-driving cars represents a paradiion, autonomous fleets hold the promise of transforming entire industries, from logistics and delivery servicor autonomous vehicles are limitless, heralding a new era of mobility that is as transformative as it is visionary.', 5, '2014-10-29 18:43:16');
insert into posts (title, content, created_by, stamp_created) values ('The Pros and Cons of Electric Vehicles: A Community Discussion', 'As electric vehicles (EVs) continue to gain traction in the automotive market, it''s essential to delve into the nuanced debate surrounding their merits and limitations. On one hand, EVs offer a promiry production and disposaric fleet in terms of infrastructure development and grid capacity. By engaging in tllenges and opportunities they present for the future of mobility. What are your thoughts on the matter? Let''s discuss!', 7, '2021-10-02 17:27:09');
insert into posts (title, content, created_by, stamp_created) values ('Exploring the World of Bikes: From Pedals to Performance', 'The realm of bikes encompasses a diverse array of two-wheeled wonders, each with its own unique characteristics and capabilities. Whether it''s the simplicity and nostalgia of a classic cruiser, the adrenaltechnological advancements to the thrill of the ride itself. Let''s saddle up and pedal our way to new horizons!', 2, '2020-10-13 23:44:03');
insert into posts (title, content, created_by, stamp_created) values ('The Evolution of Automobiles: A Journey Through Time', 'Cars, the iconic symbols of modern transportation, have undergone a remarkable evolution since their inception. From the first horseless carriages of the late 19th century to the sleek and technologically advan. From vintage classics to cutting-edge concepts, there''s always something new to discover in the world of automobiles.', 8, '2022-07-09 22:33:35');
insert into posts (title, content, created_by, stamp_created) values ('Charging Towards the Future: Embracing Electric Cars', 'As we stand at the cusp of a transportation revolution, electric cars emerge as the vanguard of sustainable mobility. With zero tailpipe emissions and a reduced carbon footprint, electric vehicles (EVs) offer ang the latest innovations, debunking myths, and discussing the opportunities and challenges that lie ahead in our journey towards a greener future.', 6, '2020-03-11 23:02:12');
insert into posts (title, content, created_by, stamp_created) values ('Embracing Winter Adventure: The Thrill of Snowmobiles', 'Snowmobiles, the exhilarating machines designed to conquer winter landscapes, evoke a sense of adventure unlike any other. From carving through fresh powder in remote wilderness areas to zipping along groomed lebrating the camaraderie that comes with being part of this vibrant community.', 6, '2023-02-18 01:02:15');
insert into posts (title, content, created_by, stamp_created) values ('Navigating the Waves: Exploring the World of Boats', 'Boats, vessels that have connected civilizations and facilitated exploration for centuries, continue to capture the imagination of adventurers and enthusiasts alike. From sleek sailboats gracefully gliding acrossitime history to the latest trends in boat design and technology. Let''s set sail and navigate the waves together!', 7, '2011-06-28 15:18:21');
insert into posts (title, content, created_by, stamp_created) values ('Taking Flight: Exploring the Skies with Planes', 'Planes, marvels of engineering that defy gravity and connect the world, represent the pinnacle of human ingenuity and ambition. From sleek passenger jets soaring high above the clouds to nimble aerobatic planes perfoon technology. Let''s spread our wings and embark on a journey of discovery together!', 2, '2022-11-08 09:27:12');
insert into posts (title, content, created_by, stamp_created) values ('Riding the Rails: A Journey Through the World of Trains', 'Trains, the timeless icons of transportation that have connected cities, nations, and continents for generations, hold a special place in the hearts of travelers and enthusiasts alike. From the romantic allutory, diverse landscapes, and fascinating technology of trains. All aboard for an unforgettable ride through the world of locomotion!', 6, '2013-08-19 23:03:43');
insert into posts (title, content, created_by, stamp_created) values ('Exploring Urban Mobility: The Role of Buses in City Life', 'Buses, the workhorses of urban transportation, play a vital role in keeping cities moving and communities connected. From bustling city centers to quiet suburban streets, buses provide an accessible and rel as we explore the essential role of buses in urban mobility, highlighting their benefits, addressing challenges, and envisioning the future of public transit in our cities.', 3, '2014-05-31 21:58:02');
insert into posts (title, content, created_by, stamp_created) values ('Beneath the City: Navigating Urban Landscapes with Subways', 'Subways, the arteries of urban transportation hidden beneath city streets, offer a fast, efficient, and reliable means of commuting for millions of urban dwellers around the world. From the iconic subway lity in densely populated areas. Join us as we descend into the depths of urban transit and explore the fascinating world of subways, e everyday experiences of riders navigating the underground maze.', 1, '2017-06-16 11:29:17');
insert into posts (title, content, created_by, stamp_created) values ('Embracing the Freedom of Two Wheels: The Thrill of Motorcycles', 'Motorcycles, the epitome of freedom and adventure on two wheels, ignite a passion for exploration and excitement in riders worldwide. From the rumble of a powerful engine to the wind in your face as ycussing the latest models and gear, and reveling in the sense of camaraderie that unites riders from all walks of life. Let''s hit the open road and embrace the freedom of two wheels together!', 10, '2020-04-18 01:46:30');
insert into posts (title, content, created_by, stamp_created) values ('Efficiency and Style: Navigating Urban Landscapes with Scooters', 'Scooters, the sleek and nimble companions of urban commuters, offer a perfect blend of efficiency and style for navigating crowded city streets. From zipping through traffic jams to effortlessly maneonmental impact to the growing trend of electric scooters and the evolving landscape of urban transportation. Let''s embark on a journey to discover the joys of scootering and embrace the freedom of city living on two wheels!', 7, '2012-12-21 22:54:51');
insert into posts (title, content, created_by, stamp_created) values ('Rediscovering Urban Exploration: The Charm of Mopeds', 'Mopeds, the versatile and charming companions of urban explorers, offer a unique blend of convenience and nostalgia for riders seeking a simpler way to navigate city streets. With their compact size and efficiethe sense of freedom and independence that comes with moped ownership. Let''s embark on a journey to embrace the spirit of adventure and rediscover the charm of mopeds in the modern urban landscape!', 8, '2011-12-15 15:48:35');
insert into posts (title, content, created_by, stamp_created) values ('Conquering the Great Outdoors: The Adventure of ATVs', 'ATVs (All-Terrain Vehicles), the rugged and versatile machines designed for off-road exploration, offer thrill-seekers an exhilarating way to conquer the great outdoors. From traversing rocky trails and muddy btest models and technologies, and celebrating the camaraderie of the ATV community. Let''s rev our engines, kick up some dirt, and embark on an unforgettable journey through the wilds on the back of an ATV!', 3, '2020-06-08 04:37:27');
insert into posts (title, content, created_by, stamp_created) values ('Cruising in Comfort: The Leisurely Pace of Golf Carts', 'Golf carts, the iconic vehicles synonymous with leisurely rounds on the green, offer a relaxed and comfortable way to navigate golf courses and resort communities. From transporting players and their gear betw the leisurely pace of golf cart culture, sharing stories of memorable rounds, discussing the latest trends in golf cart design and customization, and celebrating the simple joys of cruising in comfort. Let''s tee up our adventures and glide down the fairways in style!', 8, '2019-04-24 13:05:08');
insert into posts (title, content, created_by, stamp_created) values ('Unleashing Speed: The Thrill of Go-Karts', 'Go-karts, the nimble and adrenaline-pumping machines that bring out the racer in everyone, offer an exhilarating experience for thrill-seekers of all ages. From zooming around tight corners on indoor tracks to battling foremon and embrace the thrill of high-speed action. Join us as we explore the exciting world of go-karts, sharing st the karting community. Let''s rev our engines, hit the track, and chase victory in the fast-paced world of go-kart racing!', 4, '2019-05-25 04:03:34');
insert into posts (title, content, created_by, stamp_created) values ('Blending Efficiency and Performance: The Rise of Hybrid Vehicles', 'Hybrid vehicles, the innovative and eco-conscious automobiles that seamlessly blend electric power with traditional combustion engines, have emerged as a compelling solution to the challenges of mod embark on a journey towards cleaner, greener, and more sustainable transportation with hybrid vehicles leading the way.', 1, '2020-06-09 00:50:57');
insert into posts (title, content, created_by, stamp_created) values ('Power and Versatility: Exploring the World of Trucks', 'Trucks, the rugged workhorses of the automotive world, embody a perfect blend of power, versatility, and capability. From hauling heavy loads on construction sites to conquering off-road terrain in remote wilde and celebrating the enduring appeal of these iconic vehicles. Whether you''re a seasoned truck owner or simply fascinated by their raw power and rugged charm, there''s something for everyone to discover in the exciting world of trucks.', 1, '2023-01-29 18:11:38');
insert into posts (title, content, created_by, stamp_created) values ('Exploring Boundless Horizons: The Allure of SUVs', 'SUVs, the versatile and dynamic vehicles that blend ruggedness with comfort, continue to captivate drivers with their ability to conquer both urban streets and untamed terrain. From family road trips to off-road ad to discover new horizons and embrace the spirit of adventure with SUVs leading the way.', 9, '2020-05-30 05:41:45');
insert into posts (title, content, created_by, stamp_created) values ('Versatility on Wheels: The Essential Role of Vans', 'Vans, the unsung heroes of transportation, play a crucial role in a variety of industries and lifestyles, offering unmatched versatility and utility on four wheels. From commercial fleets to family road trips, van and highlighting their essential role in keeping our lives moving forward.', 8, '2015-06-20 15:19:31');
insert into posts (title, content, created_by, stamp_created) values ('Unleashing the Spirit of Adventure: The Thrill of Dirt Bikes', 'Dirt bikes, the adrenaline-fueled machines built for off-road excitement, ignite a passion for adventure in riders of all ages. Let''s kickstart our engines, rev up our throttles, and embark on an unforgettable journey through the exhilarating realm of dirt biking!', 5, '2015-06-15 14:20:43');

INSERT INTO tags (name)
VALUES ('bikes'),
       ('cars'),
       ('electric cars'),
       ('snowmobiles'),
       ('boats'),
       ('planes'),
       ('trains'),
       ('buses'),
       ('subways'),
       ('motorcycles'),
       ('scooters'),
       ('mopeds'),
       ('ATVs'),
       ('dirtbikes'),
       ('golf carts');


INSERT INTO roles (role)
VALUES ('ADMIN'),
       ('USER');

insert ignore into posts_tags (post_id, tag_id) values (2, 10);
insert ignore into posts_tags (post_id, tag_id) values (12, 9);
insert ignore into posts_tags (post_id, tag_id) values (4, 4);
insert ignore into posts_tags (post_id, tag_id) values (10, 5);
insert ignore into posts_tags (post_id, tag_id) values (3, 2);
insert ignore into posts_tags (post_id, tag_id) values (3, 14);
insert ignore into posts_tags (post_id, tag_id) values (8, 4);
insert ignore into posts_tags (post_id, tag_id) values (9, 13);
insert ignore into posts_tags (post_id, tag_id) values (5, 3);
insert ignore into posts_tags (post_id, tag_id) values (4, 10);
insert ignore into posts_tags (post_id, tag_id) values (11, 6);
insert ignore into posts_tags (post_id, tag_id) values (10, 15);
insert ignore into posts_tags (post_id, tag_id) values (2, 12);
insert ignore into posts_tags (post_id, tag_id) values (7, 15);
insert ignore into posts_tags (post_id, tag_id) values (12, 5);
insert ignore into posts_tags (post_id, tag_id) values (12, 4);
insert ignore into posts_tags (post_id, tag_id) values (7, 10);
insert ignore into posts_tags (post_id, tag_id) values (14, 1);
insert ignore into posts_tags (post_id, tag_id) values (9, 3);
insert ignore into posts_tags (post_id, tag_id) values (2, 8);
insert ignore into posts_tags (post_id, tag_id) values (2, 3);
insert ignore into posts_tags (post_id, tag_id) values (9, 4);
insert ignore into posts_tags (post_id, tag_id) values (4, 9);
insert ignore into posts_tags (post_id, tag_id) values (12, 2);
insert ignore into posts_tags (post_id, tag_id) values (4, 1);
insert ignore into posts_tags (post_id, tag_id) values (1, 2);
insert ignore into posts_tags (post_id, tag_id) values (14, 13);
insert ignore into posts_tags (post_id, tag_id) values (15, 2);
insert ignore into posts_tags (post_id, tag_id) values (5, 14);
insert ignore into posts_tags (post_id, tag_id) values (8, 9);

insert into users_roles (user_id, role_id) values (1, 1);
insert into users_roles (user_id, role_id) values (1, 2);
insert into users_roles (user_id, role_id) values (2, 2);
insert into users_roles (user_id, role_id) values (3, 2);
insert into users_roles (user_id, role_id) values (4, 2);
insert into users_roles (user_id, role_id) values (5, 2);
insert into users_roles (user_id, role_id) values (6, 2);
insert into users_roles (user_id, role_id) values (7, 2);
insert into users_roles (user_id, role_id) values (8, 2);
insert into users_roles (user_id, role_id) values (9, 2);
insert into users_roles (user_id, role_id) values (10, 2);
insert into users_roles (user_id, role_id) values (11, 2);
insert into users_roles (user_id, role_id) values (12, 2);
insert into users_roles (user_id, role_id) values (13, 2);
insert into users_roles (user_id, role_id) values (14, 2);
insert into users_roles (user_id, role_id) values (15, 2);
insert into users_roles (user_id, role_id) values (16, 2);
insert into users_roles (user_id, role_id) values (17, 2);
insert into users_roles (user_id, role_id) values (18, 2);
insert into users_roles (user_id, role_id) values (19, 2);
insert into users_roles (user_id, role_id) values (20, 2);
insert into users_roles (user_id, role_id) values (21, 2);
insert into users_roles (user_id, role_id) values (22, 2);
insert into users_roles (user_id, role_id) values (23, 2);
insert into users_roles (user_id, role_id) values (24, 2);
insert into users_roles (user_id, role_id) values (25, 2);
insert into users_roles (user_id, role_id) values (26, 2);
insert into users_roles (user_id, role_id) values (27, 2);
insert into users_roles (user_id, role_id) values (28, 2);
insert into users_roles (user_id, role_id) values (29, 2);
insert into users_roles (user_id, role_id) values (30, 2);

insert into comments (content, user_id, post_id, stamp_created) values ('Bikes have always been my go-to for both commuting and leisure rides. There''s something special about the feeling of freedom you get when you''re cruising down the road on two wheels!', 10, 7, '2022-12-11 06:48:48');
insert into comments (content, user_id, post_id, stamp_created) values ('I recently got into mountain biking, and it''s been such an exhilarating experience exploring trails and pushing my limits. Bikes truly offer endless opportunities for adventure!', 3, 6, '2019-09-25 18:16:30');
insert into comments (content, user_id, post_id, stamp_created) values ('I''ve been considering switching to an electric bike for my daily commute. The idea of reducing my carbon footprint while enjoying a smooth ride sounds fantastic. Anyone here have experience with e-bikes?', 5, 2, '2002-01-27 18:59:57');
insert into comments (content, user_id, post_id, stamp_created) values ('It''s fascinating to see how far automobiles have come since their inception. From the Model T to modern electric cars, the evolution of automotive technology is truly remarkable!', 5, 7, '2003-04-16 11:42:13');
insert into comments (content, user_id, post_id, stamp_created) values ('I''m a huge car enthusiast, and I love learning about the history and innovations behind different car models. Can''t wait to see what the future holds for automobiles!', 7, 8, '2012-05-03 23:22:23');
insert into comments (content, user_id, post_id, stamp_created) values ('One of my favorite things about cars is how they reflect the culture and values of their time. Each decade brings new design trends and technological advancements that shape the automotive landscape.', 2, 1, '2008-02-29 09:40:19');
insert into comments (content, user_id, post_id, stamp_created) values ('I recently made the switch to an electric car, and it''s been a game-changer! The convenience of charging at home and the smooth, quiet ride have exceeded my expectations.', 5, 8, '2023-10-11 01:43:49');
insert into comments (content, user_id, post_id, stamp_created) values ('Electric cars are definitely the way of the future. With advancements in battery technology and charging infrastructure, they''re becoming more accessible and practical for everyday use.', 1, 9, '2023-03-19 03:01:44');
insert into comments (content, user_id, post_id, stamp_created) values ('I''m curious about the environmental impact of electric cars compared to traditional gasoline vehicles. Does anyone have insights or resources on this topic?', 6, 5, '2015-10-18 17:21:21');
insert into comments (content, user_id, post_id, stamp_created) values ('There''s nothing quite like spending a day out on the water in a boat. Whether it''s fishing, waterskiing, or simply cruising along the coastline, boats offer a unique way to unwind and connect with nature.', 6, 3, '2017-01-12 03:53:25');
insert into comments (content, user_id, post_id, stamp_created) values ('I''ve always been fascinated by the variety of boats out there, from sleek sailboats to powerful motor yachts. Each type has its own charm and appeal, catering to different preferences and activities.', 3, 4, '2023-04-24 02:17:56');
insert into comments (content, user_id, post_id, stamp_created) values ('Boating is such a versatile hobby. It''s great for family outings, weekend getaways, and even as a platform for water sports like wakeboarding and tubing. Who else loves spending time on the water?', 1, 2, '2023-12-02 07:49:42');
insert into comments (content, user_id, post_id, stamp_created) values ('Flying has always been a dream of mine, and I finally got my pilot''s license last year. There''s nothing quite like the feeling of freedom and adventure you get when you''re up in the sky!', 2, 6, '2022-03-20 04:48:26');
insert into comments (content, user_id, post_id, stamp_created) values ('I''m amazed by the engineering behind airplanes and how they''re able to defy gravity and soar through the clouds. It''s truly a testament to human ingenuity and innovation.', 3, 1, '2014-02-03 10:28:20');
insert into comments (content, user_id, post_id, stamp_created) values ('I''ve been fortunate enough to travel to some incredible destinations by plane, and each flight is an adventure in itself. Whether it''s the breathtaking views or the excitement of exploring new places, flying never fails to amaze me.', 2, 5, '2007-09-29 22:12:24');
insert into comments (content, user_id, post_id, stamp_created) values ('There''s something nostalgic about train travel that I absolutely love. From the rhythmic sound of wheels on tracks to the scenic views from the window, it''s a unique experience that never gets old.', 5, 10, '2003-09-06 07:43:02');
insert into comments (content, user_id, post_id, stamp_created) values ('I''ve always been fascinated by the history of trains and how they''ve shaped the development of cities and nations. It''s incredible to think about the role they''ve played in connecting people and goods across vast distances.', 1, 3, '2016-06-01 17:25:00');
insert into comments (content, user_id, post_id, stamp_created) values ('Train journeys are my favorite way to travel. There''s a sense of romance and adventure that you just don''t get with other modes of transportation. Plus, it''s a great opportunity to unplug and relax.', 8, 8, '2007-07-18 06:57:16');
insert into comments (content, user_id, post_id, stamp_created) values ('As someone who relies on public transit, buses are a lifeline for me. They''re convenient, affordable, and help reduce traffic congestion in our cities. I''m always grateful for a reliable bus system!', 5, 4, '2017-02-28 07:39:14');
insert into comments (content, user_id, post_id, stamp_created) values ('I think buses often don''t get the credit they deserve. They play a crucial role in providing access to education, healthcare, and employment opportunities for many people, especially in underserved communities.', 1, 2, '2020-01-27 08:28:39');
insert into comments (content, user_id, post_id, stamp_created) values ('I''ve noticed that some cities are investing in electric buses to reduce emissions and improve air quality. It''s great to see initiatives like this that prioritize sustainability and public health.', 8, 1, '2015-07-27 02:56:49');
insert into comments (content, user_id, post_id, stamp_created) values ('Subways are a game-changer for city dwellers like me. They''re fast, efficient, and help alleviate traffic congestion on the streets above. Plus, there''s something cool about traveling underground!', 3, 5, '2015-12-19 03:24:35');
insert into comments (content, user_id, post_id, stamp_created) values ('I''m always amazed by the engineering feats behind subway systems, from the intricate network of tunnels to the precision of train schedules. It''s a testament to human ingenuity and urban planning.', 9, 4, '2008-10-25 12:46:26');
insert into comments (content, user_id, post_id, stamp_created) values ('Subway rides are an experience in themselves. You never know who you''ll meet or what you''ll see during your journey underground. It''s like a microcosm of city life encapsulated in each subway car.', 3, 2, '2012-02-04 03:35:46');
insert into comments (content, user_id, post_id, stamp_created) values ('Motorcycles have always been my passion. There''s something about the feeling of freedom you get when you''re riding on the open road, with the wind in your face and the rumble of the engine beneath you. It''s pure exhilaration!', 1, 3, '2007-07-25 21:23:56');
insert into comments (content, user_id, post_id, stamp_created) values ('I love the sense of community among motorcycle riders. Whether it''s sharing tips on maintenance or organizing group rides, there''s a camaraderie that''s hard to find elsewhere. It''s like being part of one big family.', 8, 8, '2009-10-18 21:57:05');
insert into comments (content, user_id, post_id, stamp_created) values ('Safety is always a concern when it comes to motorcycles, but with the right gear and responsible riding habits, it''s possible to enjoy the thrill of riding while staying safe on the road. It''s all about finding the right balance.', 5, 7, '2019-09-23 21:28:17');
insert into comments (content, user_id, post_id, stamp_created) values ('Scooters are my go-to mode of transportation for getting around the city. They''re compact, maneuverable, and perfect for navigating through traffic. Plus, they''re a lot of fun to ride!', 8, 5, '2016-06-02 02:20:11');
insert into comments (content, user_id, post_id, stamp_created) values ('I recently switched to an electric scooter for my daily commute, and it''s been a game-changer. Not only is it environmentally friendly, but it''s also super convenient and cost-effective. I wish I had made the switch sooner!', 2, 3, '2008-11-28 05:41:55');
insert into comments (content, user_id, post_id, stamp_created) values ('I think scooters are underrated when it comes to urban transportation. They''re not just for kids—they''re a practical and efficient way for adults to get around town. Plus, there are so many stylish options available now!', 1, 7, '2010-11-05 02:45:23');
insert into comments (content, user_id, post_id, stamp_created) values ('I''ve been considering getting a moped for my daily commute. They seem like a fun and economical way to navigate city streets without dealing with the hassle of parking. Any recommendations on good models to look into?', 10, 1, '2022-05-26 01:09:35');
insert into comments (content, user_id, post_id, stamp_created) values ('There''s a certain charm to riding a moped that you just don''t get with other modes of transportation. It''s like a throwback to simpler times, yet still a practical and efficient way to get around town.', 5, 4, '2014-12-15 16:47:46');
insert into comments (content, user_id, post_id, stamp_created) values ('I''ve been riding mopeds for years, and they never fail to put a smile on my face. There''s something about the laid-back vibe and ease of riding that makes every journey enjoyable. Plus, they''re a conversation starter wherever I go!', 4, 1, '2021-11-29 07:26:25');
insert into comments (content, user_id, post_id, stamp_created) values ('I grew up riding ATVs in the great outdoors, and it''s a passion that has stuck with me into adulthood. There''s nothing quite like exploring rugged terrain and conquering obstacles on an ATV. It''s a thrill like no other!', 3, 5, '2001-03-10 08:41:19');
insert into comments (content, user_id, post_id, stamp_created) values ('Safety is paramount when it comes to riding ATVs, especially off-road. Always wear the proper protective gear and ride within your limits to ensure a fun and safe experience. It''s all about enjoying the adventure responsibly.', 8, 4, '2023-07-21 04:47:50');
insert into comments (content, user_id, post_id, stamp_created) values ('I''ve been thinking about getting into ATV riding as a way to spice up my weekends. Any tips for beginners on where to start and what to look for in a good ATV?', 2, 6, '2011-01-09 01:37:10');
insert into comments (content, user_id, post_id, stamp_created) values ('Dirt biking is my ultimate adrenaline rush! There''s nothing like the feeling of tearing through dirt trails and catching air on jumps. It''s the perfect way to escape the hustle and bustle of everyday life and connect with nature.', 3, 4, '2011-08-04 03:12:32');
insert into comments (content, user_id, post_id, stamp_created) values ('I''ve been dirt biking for years, and it never gets old. There''s always a new trail to explore or a new challenge to conquer. Plus, the sense of accomplishment when you nail a difficult section is unbeatable!', 5, 8, '2010-01-14 14:47:16');
insert into comments (content, user_id, post_id, stamp_created) values ('Safety is key when it comes to dirt biking, especially for beginners. Always wear the proper protective gear, start with easy trails to build your skills, and never ride beyond your comfort level. It''s all about having fun while staying safe!', 3, 3, '2013-12-15 00:15:09');
insert into comments (content, user_id, post_id, stamp_created) values ('Golf carts are a staple at my local course, and they''re always a highlight of my golfing experience. There''s something relaxing about cruising from hole to hole, enjoying the scenery and camaraderie with friends.', 7, 3, '2001-05-25 23:08:08');
insert into comments (content, user_id, post_id, stamp_created) values ('I love how customizable golf carts can be. From tricked-out models with custom paint jobs to practical accessories like coolers and storage bins, there''s no limit to how you can personalize your ride.', 4, 1, '2013-12-29 18:56:59');
insert into comments (content, user_id, post_id, stamp_created) values ('Golf carts aren''t just for golfing anymore—I''ve seen them used for everything from neighborhood transportation to campground cruising. They''re a versatile and convenient way to get around, no matter where you are!', 2, 5, '2002-12-27 17:36:58');
insert into comments (content, user_id, post_id, stamp_created) values ('I recently switched to a hybrid vehicle, and it''s been a game-changer for me. Not only am I saving money on fuel, but I also feel good knowing that I''m reducing my carbon footprint. It''s the best of both worlds!', 6, 2, '2006-06-07 16:58:11');
insert into comments (content, user_id, post_id, stamp_created) values ('I''m excited to see more automakers investing in hybrid technology. With advancements in battery technology and improved efficiency, hybrids are becoming a practical and sustainable choice for drivers everywhere.', 5, 3, '2020-02-20 11:41:53');
insert into comments (content, user_id, post_id, stamp_created) values ('One of the things I love most about hybrids is how seamlessly they switch between electric and gasoline power. It''s amazing how efficient they can be, especially in stop-and-go city traffic. Plus, the regenerative braking feature is a neat bonus!', 4, 9, '2017-11-20 01:34:16');
insert into comments (content, user_id, post_id, stamp_created) values ('Trucks are a necessity for me—I use mine for everything from hauling lumber to towing my boat. They''re like a Swiss Army knife on wheels, ready for any task I throw at them.', 9, 3, '2004-12-26 23:47:39');
insert into comments (content, user_id, post_id, stamp_created) values ('I love how trucks have evolved over the years. From basic workhorses to luxury vehicles with all the bells and whistles, there''s a truck for every lifestyle and budget.', 8, 9, '2013-06-22 13:32:58');
insert into comments (content, user_id, post_id, stamp_created) values ('I''ve always been impressed by the towing capabilities of trucks. Whether it''s pulling a camper or hauling a heavy load, they never seem to break a sweat. It''s no wonder they''re the go-to choice for so many people!', 3, 1, '2020-08-31 15:42:02');
insert into comments (content, user_id, post_id, stamp_created) values ('Vans are the unsung heroes of transportation. I don''t know what I''d do without mine—it''s my mobile office, weekend getaway vehicle, and family hauler all in one!', 8, 2, '2010-07-16 20:59:00');
insert into comments (content, user_id, post_id, stamp_created) values ('I love how customizable vans can be. Whether you need extra seating, storage space, or even a kitchenette, there''s a van conversion option out there for you. It''s like having a blank canvas to create your dream vehicle!', 1, 7, '2010-08-11 22:18:15');

insert ignore into likes_dislikes (post_id, user_id, like_dislike) values (5, 26, 'LIKE');
insert ignore into likes_dislikes (post_id, user_id, like_dislike) values (5, 22, 'DISLIKE');
insert ignore into likes_dislikes (post_id, user_id, like_dislike) values (9, 9, 'DISLIKE');
insert ignore into likes_dislikes (post_id, user_id, like_dislike) values (4, 23, 'LIKE');
insert ignore into likes_dislikes (post_id, user_id, like_dislike) values (8, 27, 'DISLIKE');
insert ignore into likes_dislikes (post_id, user_id, like_dislike) values (8, 14, 'DISLIKE');
insert ignore into likes_dislikes (post_id, user_id, like_dislike) values (2, 23, 'LIKE');
insert ignore into likes_dislikes (post_id, user_id, like_dislike) values (8, 2, 'DISLIKE');
insert ignore into likes_dislikes (post_id, user_id, like_dislike) values (3, 20, 'LIKE');
insert ignore into likes_dislikes (post_id, user_id, like_dislike) values (6, 9, 'LIKE');
insert ignore into likes_dislikes (post_id, user_id, like_dislike) values (9, 3, 'LIKE');
insert ignore into likes_dislikes (post_id, user_id, like_dislike) values (4, 6, 'DISLIKE');
insert ignore into likes_dislikes (post_id, user_id, like_dislike) values (4, 13, 'LIKE');
insert ignore into likes_dislikes (post_id, user_id, like_dislike) values (8, 4, 'LIKE');
insert ignore into likes_dislikes (post_id, user_id, like_dislike) values (4, 19, 'LIKE');
insert ignore into likes_dislikes (post_id, user_id, like_dislike) values (1, 12, 'DISLIKE');
insert ignore into likes_dislikes (post_id, user_id, like_dislike) values (5, 3, 'LIKE');
insert ignore into likes_dislikes (post_id, user_id, like_dislike) values (8, 11, 'DISLIKE');
insert ignore into likes_dislikes (post_id, user_id, like_dislike) values (8, 19, 'LIKE');
insert ignore into likes_dislikes (post_id, user_id, like_dislike) values (6, 8, 'DISLIKE');
insert ignore into likes_dislikes (post_id, user_id, like_dislike) values (9, 16, 'LIKE');
insert ignore into likes_dislikes (post_id, user_id, like_dislike) values (8, 9, 'DISLIKE');
insert ignore into likes_dislikes (post_id, user_id, like_dislike) values (2, 1, 'DISLIKE');
insert ignore into likes_dislikes (post_id, user_id, like_dislike) values (4, 22, 'DISLIKE');
insert ignore into likes_dislikes (post_id, user_id, like_dislike) values (6, 21, 'DISLIKE');
insert ignore into likes_dislikes (post_id, user_id, like_dislike) values (4, 10, 'LIKE');
insert ignore into likes_dislikes (post_id, user_id, like_dislike) values (10, 18, 'LIKE');
insert ignore into likes_dislikes (post_id, user_id, like_dislike) values (1, 13, 'DISLIKE');
insert ignore into likes_dislikes (post_id, user_id, like_dislike) values (10, 14, 'LIKE');
insert ignore into likes_dislikes (post_id, user_id, like_dislike) values (3, 20, 'DISLIKE');
insert ignore into likes_dislikes (post_id, user_id, like_dislike) values (8, 11, 'LIKE');
insert ignore into likes_dislikes (post_id, user_id, like_dislike) values (6, 23, 'LIKE');
insert ignore into likes_dislikes (post_id, user_id, like_dislike) values (5, 2, 'LIKE');
insert ignore into likes_dislikes (post_id, user_id, like_dislike) values (1, 9, 'DISLIKE');
insert ignore into likes_dislikes (post_id, user_id, like_dislike) values (8, 18, 'DISLIKE');
insert ignore into likes_dislikes (post_id, user_id, like_dislike) values (3, 29, 'DISLIKE');
insert ignore into likes_dislikes (post_id, user_id, like_dislike) values (3, 22, 'DISLIKE');
insert ignore into likes_dislikes (post_id, user_id, like_dislike) values (8, 17, 'DISLIKE');
insert ignore into likes_dislikes (post_id, user_id, like_dislike) values (9, 23, 'LIKE');
insert ignore into likes_dislikes (post_id, user_id, like_dislike) values (8, 16, 'LIKE');
insert ignore into likes_dislikes (post_id, user_id, like_dislike) values (6, 30, 'DISLIKE');
insert ignore into likes_dislikes (post_id, user_id, like_dislike) values (3, 29, 'DISLIKE');
insert ignore into likes_dislikes (post_id, user_id, like_dislike) values (8, 3, 'LIKE');
insert ignore into likes_dislikes (post_id, user_id, like_dislike) values (7, 23, 'LIKE');
insert ignore into likes_dislikes (post_id, user_id, like_dislike) values (7, 2, 'DISLIKE');
insert ignore into likes_dislikes (post_id, user_id, like_dislike) values (9, 6, 'LIKE');
insert ignore into likes_dislikes (post_id, user_id, like_dislike) values (6, 6, 'LIKE');
insert ignore into likes_dislikes (post_id, user_id, like_dislike) values (9, 24, 'DISLIKE');
insert ignore into likes_dislikes (post_id, user_id, like_dislike) values (4, 2, 'DISLIKE');
insert ignore into likes_dislikes (post_id, user_id, like_dislike) values (2, 3, 'LIKE');
insert ignore into likes_dislikes (post_id, user_id, like_dislike) values (9, 5, 'DISLIKE');
insert ignore into likes_dislikes (post_id, user_id, like_dislike) values (3, 12, 'LIKE');
insert ignore into likes_dislikes (post_id, user_id, like_dislike) values (9, 22, 'LIKE');
insert ignore into likes_dislikes (post_id, user_id, like_dislike) values (8, 4, 'DISLIKE');
insert ignore into likes_dislikes (post_id, user_id, like_dislike) values (8, 30, 'LIKE');
insert ignore into likes_dislikes (post_id, user_id, like_dislike) values (2, 6, 'DISLIKE');
insert ignore into likes_dislikes (post_id, user_id, like_dislike) values (5, 18, 'LIKE');
insert ignore into likes_dislikes (post_id, user_id, like_dislike) values (8, 16, 'LIKE');
insert ignore into likes_dislikes (post_id, user_id, like_dislike) values (4, 4, 'DISLIKE');
insert ignore into likes_dislikes (post_id, user_id, like_dislike) values (2, 2, 'DISLIKE');
insert ignore into likes_dislikes (post_id, user_id, like_dislike) values (3, 20, 'DISLIKE');
insert ignore into likes_dislikes (post_id, user_id, like_dislike) values (3, 28, 'LIKE');
insert ignore into likes_dislikes (post_id, user_id, like_dislike) values (10, 8, 'LIKE');
insert ignore into likes_dislikes (post_id, user_id, like_dislike) values (7, 26, 'LIKE');
insert ignore into likes_dislikes (post_id, user_id, like_dislike) values (9, 26, 'LIKE');
insert ignore into likes_dislikes (post_id, user_id, like_dislike) values (10, 11, 'LIKE');
insert ignore into likes_dislikes (post_id, user_id, like_dislike) values (7, 1, 'DISLIKE');
insert ignore into likes_dislikes (post_id, user_id, like_dislike) values (9, 21, 'LIKE');
insert ignore into likes_dislikes (post_id, user_id, like_dislike) values (1, 26, 'LIKE');
insert ignore into likes_dislikes (post_id, user_id, like_dislike) values (10, 30, 'LIKE');
insert ignore into likes_dislikes (post_id, user_id, like_dislike) values (1, 26, 'LIKE');
insert ignore into likes_dislikes (post_id, user_id, like_dislike) values (1, 7, 'LIKE');
insert ignore into likes_dislikes (post_id, user_id, like_dislike) values (2, 21, 'DISLIKE');
insert ignore into likes_dislikes (post_id, user_id, like_dislike) values (3, 5, 'LIKE');
insert ignore into likes_dislikes (post_id, user_id, like_dislike) values (7, 17, 'LIKE');
insert ignore into likes_dislikes (post_id, user_id, like_dislike) values (8, 29, 'LIKE');
insert ignore into likes_dislikes (post_id, user_id, like_dislike) values (2, 8, 'LIKE');
insert ignore into likes_dislikes (post_id, user_id, like_dislike) values (1, 20, 'DISLIKE');
insert ignore into likes_dislikes (post_id, user_id, like_dislike) values (6, 28, 'LIKE');
insert ignore into likes_dislikes (post_id, user_id, like_dislike) values (9, 4, 'DISLIKE');
insert ignore into likes_dislikes (post_id, user_id, like_dislike) values (4, 7, 'DISLIKE');
insert ignore into likes_dislikes (post_id, user_id, like_dislike) values (4, 4, 'LIKE');
insert ignore into likes_dislikes (post_id, user_id, like_dislike) values (5, 24, 'DISLIKE');
insert ignore into likes_dislikes (post_id, user_id, like_dislike) values (5, 8, 'DISLIKE');
insert ignore into likes_dislikes (post_id, user_id, like_dislike) values (5, 5, 'DISLIKE');
insert ignore into likes_dislikes (post_id, user_id, like_dislike) values (2, 4, 'DISLIKE');
insert ignore into likes_dislikes (post_id, user_id, like_dislike) values (9, 9, 'LIKE');
insert ignore into likes_dislikes (post_id, user_id, like_dislike) values (1, 2, 'DISLIKE');
insert ignore into likes_dislikes (post_id, user_id, like_dislike) values (9, 26, 'LIKE');
insert ignore into likes_dislikes (post_id, user_id, like_dislike) values (8, 13, 'DISLIKE');
insert ignore into likes_dislikes (post_id, user_id, like_dislike) values (1, 24, 'LIKE');
insert ignore into likes_dislikes (post_id, user_id, like_dislike) values (8, 8, 'DISLIKE');
insert ignore into likes_dislikes (post_id, user_id, like_dislike) values (1, 24, 'LIKE');
insert ignore into likes_dislikes (post_id, user_id, like_dislike) values (9, 19, 'DISLIKE');
insert ignore into likes_dislikes (post_id, user_id, like_dislike) values (10, 22, 'LIKE');
insert ignore into likes_dislikes (post_id, user_id, like_dislike) values (8, 11, 'LIKE');
insert ignore into likes_dislikes (post_id, user_id, like_dislike) values (3, 23, 'DISLIKE');
insert ignore into likes_dislikes (post_id, user_id, like_dislike) values (4, 17, 'DISLIKE');
insert ignore into likes_dislikes (post_id, user_id, like_dislike) values (8, 15, 'LIKE');
insert ignore into likes_dislikes (post_id, user_id, like_dislike) values (3, 24, 'DISLIKE');


insert into phones (user_id, phone) VALUES (1, '0888123456');

INSERT INTO pictures (user_id, picture) VALUES (1, 'https://res.cloudinary.com/dhixuwvrk/image/upload/v1708300140/w4jmj7vjkwtmbcwrqhiu.png');
INSERT INTO pictures (user_id, picture) VALUES (2, 'https://res.cloudinary.com/dhixuwvrk/image/upload/v1708300140/ytkgw9c4xu4mytskglbh.png');
INSERT INTO pictures (user_id, picture) VALUES (3, 'https://res.cloudinary.com/dhixuwvrk/image/upload/v1708300139/j1wn5yg3s7swdkqospcb.png');
INSERT INTO pictures (user_id, picture) VALUES (4, 'https://res.cloudinary.com/dhixuwvrk/image/upload/v1708300139/l3xkfndmbjp4xtjuxjoy.png');
INSERT INTO pictures (user_id, picture) VALUES (5, 'https://res.cloudinary.com/dhixuwvrk/image/upload/v1708300139/i77ujuzr4gargz1z6u5a.png');
INSERT INTO pictures (user_id, picture) VALUES (6, 'https://res.cloudinary.com/dhixuwvrk/image/upload/v1708300139/j6v2kljvjkxfti4n2apt.png');
INSERT INTO pictures (user_id, picture) VALUES (7, 'https://res.cloudinary.com/dhixuwvrk/image/upload/v1708300139/ppll4rfzsoqjmop2vzlc.png');
INSERT INTO pictures (user_id, picture) VALUES (8, 'https://res.cloudinary.com/dhixuwvrk/image/upload/v1708300139/kmfvierpvfjoke4hnksy.png');
INSERT INTO pictures (user_id, picture) VALUES (9, 'https://res.cloudinary.com/dhixuwvrk/image/upload/v1708300139/zv8l5clt3cjnputgotbh.png');
INSERT INTO pictures (user_id, picture) VALUES (10, 'https://res.cloudinary.com/dhixuwvrk/image/upload/v1708300139/yhp81fykqk91z9bbvsib.png');

