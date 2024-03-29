begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.facebook
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|facebook
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Locale
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|facebook4j
operator|.
name|TestUser
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|builder
operator|.
name|RouteBuilder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_comment
comment|/**  * Test methods in {@link facebook4j.api.TestUserMethods}  */
end_comment

begin_class
DECL|class|TestUserMethodsTest
specifier|public
class|class
name|TestUserMethodsTest
extends|extends
name|CamelFacebookTestSupport
block|{
DECL|field|TEST_USER1
specifier|private
specifier|static
specifier|final
name|String
name|TEST_USER1
init|=
literal|"test one"
decl_stmt|;
DECL|field|TEST_USER2
specifier|private
specifier|static
specifier|final
name|String
name|TEST_USER2
init|=
literal|"test two"
decl_stmt|;
DECL|method|TestUserMethodsTest ()
specifier|public
name|TestUserMethodsTest
parameter_list|()
throws|throws
name|Exception
block|{     }
annotation|@
name|Test
DECL|method|testTestUsers ()
specifier|public
name|void
name|testTestUsers
parameter_list|()
block|{
comment|// create a test user with exchange properties
specifier|final
name|TestUser
name|testUser1
init|=
name|template
argument_list|()
operator|.
name|requestBody
argument_list|(
literal|"direct:createTestUser"
argument_list|,
name|TEST_USER1
argument_list|,
name|TestUser
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Test User1"
argument_list|,
name|testUser1
argument_list|)
expr_stmt|;
comment|// create a test user with exchange properties
specifier|final
name|TestUser
name|testUser2
init|=
name|template
argument_list|()
operator|.
name|requestBody
argument_list|(
literal|"direct:createTestUser"
argument_list|,
name|TEST_USER2
argument_list|,
name|TestUser
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Test User2"
argument_list|,
name|testUser2
argument_list|)
expr_stmt|;
comment|// make friends, not enemies
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|headers
operator|.
name|put
argument_list|(
literal|"CamelFacebook.testUser2"
argument_list|,
name|testUser2
argument_list|)
expr_stmt|;
name|Boolean
name|worked
init|=
name|template
argument_list|()
operator|.
name|requestBodyAndHeaders
argument_list|(
literal|"direct:makeFriendTestUser"
argument_list|,
name|testUser1
argument_list|,
name|headers
argument_list|,
name|Boolean
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Friends not made"
argument_list|,
name|worked
argument_list|)
expr_stmt|;
comment|// get app test users
specifier|final
name|List
name|testUsers
init|=
name|template
argument_list|()
operator|.
name|requestBody
argument_list|(
literal|"direct:testUsers"
argument_list|,
literal|null
argument_list|,
name|List
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Test users"
argument_list|,
name|testUsers
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"Empty test user list"
argument_list|,
name|testUsers
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
comment|// delete test users
for|for
control|(
name|Object
name|user
range|:
name|testUsers
control|)
block|{
specifier|final
name|TestUser
name|testUser
init|=
operator|(
name|TestUser
operator|)
name|user
decl_stmt|;
if|if
condition|(
name|testUser
operator|.
name|equals
argument_list|(
name|testUser1
argument_list|)
operator|||
name|testUser
operator|.
name|equals
argument_list|(
name|testUser2
argument_list|)
condition|)
block|{
specifier|final
name|String
name|id
init|=
name|testUser
operator|.
name|getId
argument_list|()
decl_stmt|;
name|worked
operator|=
name|template
argument_list|()
operator|.
name|requestBody
argument_list|(
literal|"direct:deleteTestUser"
argument_list|,
name|id
argument_list|,
name|Boolean
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Test user not deleted for id "
operator|+
name|id
argument_list|,
name|worked
argument_list|)
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Override
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|RouteBuilder
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|from
argument_list|(
literal|"direct:createTestUser"
argument_list|)
operator|.
name|to
argument_list|(
literal|"facebook://createTestUser?inBody=name&appId="
operator|+
name|properties
operator|.
name|get
argument_list|(
literal|"oAuthAppId"
argument_list|)
operator|+
literal|"&userLocale="
operator|+
name|Locale
operator|.
name|getDefault
argument_list|()
operator|.
name|toString
argument_list|()
operator|+
literal|"&permissions="
operator|+
name|getTestPermissions
argument_list|()
operator|+
literal|"&"
operator|+
name|getAppOauthParams
argument_list|()
argument_list|)
expr_stmt|;
comment|// note short form testUsers instead of getTestUsers
name|from
argument_list|(
literal|"direct:testUsers"
argument_list|)
operator|.
name|to
argument_list|(
literal|"facebook://testUsers?appId="
operator|+
name|properties
operator|.
name|get
argument_list|(
literal|"oAuthAppId"
argument_list|)
operator|+
literal|"&"
operator|+
name|getAppOauthParams
argument_list|()
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:makeFriendTestUser"
argument_list|)
operator|.
name|to
argument_list|(
literal|"facebook://makeFriendTestUser?inBody=testUser1&"
operator|+
name|getAppOauthParams
argument_list|()
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:deleteTestUser"
argument_list|)
operator|.
name|to
argument_list|(
literal|"facebook://deleteTestUser?inBody=testUserId&"
operator|+
name|getAppOauthParams
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|method|getTestPermissions ()
specifier|public
name|String
name|getTestPermissions
parameter_list|()
block|{
return|return
literal|"email"
operator|+
literal|",publish_actions"
operator|+
literal|",user_about_me"
operator|+
literal|",user_activities"
operator|+
literal|",user_birthday"
operator|+
literal|",user_checkins"
operator|+
literal|",user_education_history"
operator|+
literal|",user_events"
operator|+
literal|",user_games_activity"
operator|+
literal|",user_groups"
operator|+
literal|",user_hometown"
operator|+
literal|",user_interests"
operator|+
literal|",user_likes"
operator|+
literal|",user_location"
operator|+
literal|",user_notes"
operator|+
literal|",user_photos"
operator|+
literal|",user_questions"
operator|+
literal|",user_relationship_details"
operator|+
literal|",user_relationships"
operator|+
literal|",user_religion_politics"
operator|+
literal|",user_status"
operator|+
literal|",user_subscriptions"
operator|+
literal|",user_videos"
operator|+
literal|",user_website"
operator|+
literal|",user_work_history"
operator|+
literal|",friends_about_me"
operator|+
literal|",friends_activities"
operator|+
literal|",friends_birthday"
operator|+
literal|",friends_checkins"
operator|+
literal|",friends_education_history"
operator|+
literal|",friends_events"
operator|+
literal|",friends_games_activity"
operator|+
literal|",friends_groups"
operator|+
literal|",friends_hometown"
operator|+
literal|",friends_interests"
operator|+
literal|",friends_likes"
operator|+
literal|",friends_location"
operator|+
literal|",friends_notes"
operator|+
literal|",friends_photos"
operator|+
literal|",friends_questions"
operator|+
literal|",friends_relationship_details"
operator|+
literal|",friends_relationships"
operator|+
literal|",friends_religion_politics"
operator|+
literal|",friends_status"
operator|+
literal|",friends_subscriptions"
operator|+
literal|",friends_videos"
operator|+
literal|",friends_website"
operator|+
literal|",friends_work_history"
operator|+
literal|",ads_management"
operator|+
literal|",create_event"
operator|+
literal|",create_note"
operator|+
literal|",export_stream"
operator|+
literal|",friends_online_presence"
operator|+
literal|",manage_friendlists"
operator|+
literal|",manage_notifications"
operator|+
literal|",manage_pages"
operator|+
literal|",photo_upload"
operator|+
literal|",publish_checkins"
operator|+
literal|",publish_stream"
operator|+
literal|",read_friendlists"
operator|+
literal|",read_insights"
operator|+
literal|",read_mailbox"
operator|+
literal|",read_page_mailboxes"
operator|+
literal|",read_requests"
operator|+
literal|",read_stream"
operator|+
literal|",rsvp_event"
operator|+
literal|",share_item"
operator|+
literal|",sms"
operator|+
literal|",status_update"
operator|+
literal|",user_online_presence"
operator|+
literal|",video_upload"
operator|+
literal|",xmpp_login"
return|;
block|}
block|}
end_class

end_unit

