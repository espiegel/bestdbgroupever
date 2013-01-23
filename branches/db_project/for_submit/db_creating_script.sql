-- phpMyAdmin SQL Dump
-- version 3.3.2deb1
-- http://www.phpmyadmin.net
--
-- מארח: mysqlsrv.cs.tau.ac.il
-- זמן ייצור: ינואר 23, 2013 at 09:06 PM
-- גרסת שרת: 5.1.41
-- גרסת PHP: 5.3.2-1ubuntu4

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- מאגר נתונים: `DbMysql02`
--

-- --------------------------------------------------------

--
-- מבנה טבלה עבור טבלה `Actors`
--

CREATE TABLE IF NOT EXISTS `Actors` (
  `actor_id` int(11) NOT NULL AUTO_INCREMENT,
  `freebase_id` varchar(45) DEFAULT NULL,
  `name` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`actor_id`),
  KEY `FREEBASE` (`freebase_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=78523 ;

-- --------------------------------------------------------

--
-- מבנה טבלה עבור טבלה `ActorsInMedia`
--

CREATE TABLE IF NOT EXISTS `ActorsInMedia` (
  `actor_id` int(11) NOT NULL,
  `media_id` int(11) NOT NULL,
  `char_name` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`actor_id`,`media_id`),
  KEY `actor_id_idx` (`actor_id`),
  KEY `media_id_idx` (`media_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- מבנה טבלה עבור טבלה `CommentOfUser`
--

CREATE TABLE IF NOT EXISTS `CommentOfUser` (
  `comment_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `vote` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`comment_id`,`user_id`),
  KEY `comment_id` (`comment_id`),
  KEY `user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- מבנה טבלה עבור טבלה `Comments`
--

CREATE TABLE IF NOT EXISTS `Comments` (
  `comment_id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `location_id` int(11) DEFAULT NULL,
  `comment` varchar(45) DEFAULT NULL,
  `upvotes` int(11) DEFAULT '0',
  `downvotes` int(11) DEFAULT '0',
  `is_check_in` tinyint(1) DEFAULT NULL,
  `photo` varchar(45) DEFAULT NULL,
  `date` datetime NOT NULL,
  PRIMARY KEY (`comment_id`),
  KEY `user_id_idx` (`user_id`),
  KEY `location_id_idx` (`location_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=41 ;

-- --------------------------------------------------------

--
-- מבנה טבלה עבור טבלה `Films`
--

CREATE TABLE IF NOT EXISTS `Films` (
  `media_id` int(11) NOT NULL,
  `release_date` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`media_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- מבנה טבלה עבור טבלה `ImageBinaries`
--

CREATE TABLE IF NOT EXISTS `ImageBinaries` (
  `media_id` int(11) NOT NULL,
  `image` blob NOT NULL,
  PRIMARY KEY (`media_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- מבנה טבלה עבור טבלה `LocationOfMedia`
--

CREATE TABLE IF NOT EXISTS `LocationOfMedia` (
  `media_id` int(11) NOT NULL,
  `location_id` int(11) NOT NULL,
  `scene_episode` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`media_id`,`location_id`),
  KEY `media_id_idx` (`media_id`),
  KEY `location_id_idx` (`location_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- מבנה טבלה עבור טבלה `Locations`
--

CREATE TABLE IF NOT EXISTS `Locations` (
  `location_id` int(11) NOT NULL AUTO_INCREMENT,
  `lat` varchar(45) NOT NULL,
  `lng` varchar(45) NOT NULL,
  `country` varchar(45) DEFAULT NULL,
  `city` varchar(45) DEFAULT NULL,
  `place` varchar(45) DEFAULT NULL,
  `upvotes` int(11) DEFAULT '0',
  `downvotes` int(11) DEFAULT '0',
  PRIMARY KEY (`location_id`,`lat`,`lng`),
  KEY `coord` (`lat`,`lng`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=5239 ;

-- --------------------------------------------------------

--
-- מבנה טבלה עבור טבלה `Media`
--

CREATE TABLE IF NOT EXISTS `Media` (
  `media_id` int(11) NOT NULL AUTO_INCREMENT,
  `freebase_id` varchar(45) DEFAULT NULL,
  `name` varchar(45) DEFAULT NULL,
  `directors` varchar(45) DEFAULT NULL,
  `image` varchar(45) DEFAULT NULL,
  `isTv` int(11) DEFAULT NULL,
  PRIMARY KEY (`media_id`),
  KEY `FREEBASE` (`freebase_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=39015 ;

-- --------------------------------------------------------

--
-- מבנה טבלה עבור טבלה `TV`
--

CREATE TABLE IF NOT EXISTS `TV` (
  `media_id` int(11) NOT NULL,
  `first_episode` varchar(45) DEFAULT NULL,
  `last_episode` varchar(45) DEFAULT NULL,
  `num_seasons` int(11) DEFAULT NULL,
  `num_episodes` int(11) DEFAULT NULL,
  PRIMARY KEY (`media_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- מבנה טבלה עבור טבלה `Users`
--

CREATE TABLE IF NOT EXISTS `Users` (
  `user_id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) DEFAULT NULL,
  `password` varchar(45) DEFAULT NULL,
  `upvotes` int(11) DEFAULT '0',
  `downvotes` int(11) DEFAULT '0',
  `is_admin` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`user_id`),
  KEY `name` (`name`),
  KEY `password` (`password`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 COMMENT='This is the table for all users of the application' AUTO_INCREMENT=10 ;

--
-- הגבלות לטבלאות שהוצאו
--

--
-- הגבלות לטבלה `ActorsInMedia`
--
ALTER TABLE `ActorsInMedia`
  ADD CONSTRAINT `ActorsInMedia_ibfk_1` FOREIGN KEY (`actor_id`) REFERENCES `Actors` (`actor_id`) ON DELETE CASCADE ON UPDATE NO ACTION,
  ADD CONSTRAINT `actorsinmedia2mediaid` FOREIGN KEY (`media_id`) REFERENCES `Media` (`media_id`) ON DELETE CASCADE ON UPDATE NO ACTION;

--
-- הגבלות לטבלה `CommentOfUser`
--
ALTER TABLE `CommentOfUser`
  ADD CONSTRAINT `CommentOfUser_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `Users` (`user_id`) ON DELETE CASCADE,
  ADD CONSTRAINT `CommentOfUser_ibfk_1` FOREIGN KEY (`comment_id`) REFERENCES `Comments` (`comment_id`) ON DELETE CASCADE;

--
-- הגבלות לטבלה `Comments`
--
ALTER TABLE `Comments`
  ADD CONSTRAINT `location_id` FOREIGN KEY (`location_id`) REFERENCES `Locations` (`location_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `user_id` FOREIGN KEY (`user_id`) REFERENCES `Users` (`user_id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- הגבלות לטבלה `Films`
--
ALTER TABLE `Films`
  ADD CONSTRAINT `film2media` FOREIGN KEY (`media_id`) REFERENCES `Media` (`media_id`) ON DELETE CASCADE ON UPDATE NO ACTION;

--
-- הגבלות לטבלה `ImageBinaries`
--
ALTER TABLE `ImageBinaries`
  ADD CONSTRAINT `ImageBinaries_ibfk_1` FOREIGN KEY (`media_id`) REFERENCES `Media` (`media_id`) ON DELETE CASCADE;

--
-- הגבלות לטבלה `LocationOfMedia`
--
ALTER TABLE `LocationOfMedia`
  ADD CONSTRAINT `locationofmedia2location_id` FOREIGN KEY (`location_id`) REFERENCES `Locations` (`location_id`) ON DELETE CASCADE ON UPDATE NO ACTION,
  ADD CONSTRAINT `locationofmedia2media_id` FOREIGN KEY (`media_id`) REFERENCES `Media` (`media_id`) ON DELETE CASCADE ON UPDATE NO ACTION;

--
-- הגבלות לטבלה `TV`
--
ALTER TABLE `TV`
  ADD CONSTRAINT `media_id` FOREIGN KEY (`media_id`) REFERENCES `Media` (`media_id`) ON DELETE CASCADE ON UPDATE NO ACTION;
