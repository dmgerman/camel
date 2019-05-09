begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.yammer.model
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|yammer
operator|.
name|model
package|;
end_package

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
name|com
operator|.
name|fasterxml
operator|.
name|jackson
operator|.
name|annotation
operator|.
name|JsonIgnoreProperties
import|;
end_import

begin_import
import|import
name|com
operator|.
name|fasterxml
operator|.
name|jackson
operator|.
name|annotation
operator|.
name|JsonProperty
import|;
end_import

begin_class
annotation|@
name|JsonIgnoreProperties
argument_list|(
name|ignoreUnknown
operator|=
literal|true
argument_list|)
DECL|class|Meta
specifier|public
class|class
name|Meta
block|{
annotation|@
name|JsonProperty
argument_list|(
literal|"feed_desc"
argument_list|)
DECL|field|feedDesc
specifier|private
name|String
name|feedDesc
decl_stmt|;
annotation|@
name|JsonProperty
argument_list|(
literal|"current_user_id"
argument_list|)
DECL|field|currentUserId
specifier|private
name|Long
name|currentUserId
decl_stmt|;
annotation|@
name|JsonProperty
argument_list|(
literal|"requested_poll_interval"
argument_list|)
DECL|field|requestedPollInterval
specifier|private
name|Long
name|requestedPollInterval
decl_stmt|;
annotation|@
name|JsonProperty
argument_list|(
literal|"older_available"
argument_list|)
DECL|field|olderAvailable
specifier|private
name|Boolean
name|olderAvailable
decl_stmt|;
annotation|@
name|JsonProperty
argument_list|(
literal|"followed_references"
argument_list|)
DECL|field|followedReferences
specifier|private
name|List
argument_list|<
name|FollowedReference
argument_list|>
name|followedReferences
decl_stmt|;
DECL|field|ymodules
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|ymodules
decl_stmt|;
annotation|@
name|JsonProperty
argument_list|(
literal|"liked_message_ids"
argument_list|)
DECL|field|likedMessageIds
specifier|private
name|List
argument_list|<
name|Long
argument_list|>
name|likedMessageIds
decl_stmt|;
annotation|@
name|JsonProperty
argument_list|(
literal|"feed_name"
argument_list|)
DECL|field|feedName
specifier|private
name|String
name|feedName
decl_stmt|;
DECL|field|realtime
specifier|private
name|Realtime
name|realtime
decl_stmt|;
annotation|@
name|JsonProperty
argument_list|(
literal|"direct_from_body"
argument_list|)
DECL|field|directFromBody
specifier|private
name|Boolean
name|directFromBody
decl_stmt|;
DECL|method|getFeedDesc ()
specifier|public
name|String
name|getFeedDesc
parameter_list|()
block|{
return|return
name|feedDesc
return|;
block|}
DECL|method|setFeedDesc (String feedDesc)
specifier|public
name|void
name|setFeedDesc
parameter_list|(
name|String
name|feedDesc
parameter_list|)
block|{
name|this
operator|.
name|feedDesc
operator|=
name|feedDesc
expr_stmt|;
block|}
DECL|method|getCurrentUserId ()
specifier|public
name|Long
name|getCurrentUserId
parameter_list|()
block|{
return|return
name|currentUserId
return|;
block|}
DECL|method|setCurrentUserId (Long currentUserId)
specifier|public
name|void
name|setCurrentUserId
parameter_list|(
name|Long
name|currentUserId
parameter_list|)
block|{
name|this
operator|.
name|currentUserId
operator|=
name|currentUserId
expr_stmt|;
block|}
DECL|method|getRequestedPollInterval ()
specifier|public
name|Long
name|getRequestedPollInterval
parameter_list|()
block|{
return|return
name|requestedPollInterval
return|;
block|}
DECL|method|setRequestedPollInterval (Long requestedPollInterval)
specifier|public
name|void
name|setRequestedPollInterval
parameter_list|(
name|Long
name|requestedPollInterval
parameter_list|)
block|{
name|this
operator|.
name|requestedPollInterval
operator|=
name|requestedPollInterval
expr_stmt|;
block|}
DECL|method|getOlderAvailable ()
specifier|public
name|Boolean
name|getOlderAvailable
parameter_list|()
block|{
return|return
name|olderAvailable
return|;
block|}
DECL|method|setOlderAvailable (Boolean olderAvailable)
specifier|public
name|void
name|setOlderAvailable
parameter_list|(
name|Boolean
name|olderAvailable
parameter_list|)
block|{
name|this
operator|.
name|olderAvailable
operator|=
name|olderAvailable
expr_stmt|;
block|}
DECL|method|getFollowedReferences ()
specifier|public
name|List
argument_list|<
name|FollowedReference
argument_list|>
name|getFollowedReferences
parameter_list|()
block|{
return|return
name|followedReferences
return|;
block|}
DECL|method|setFollowedReferences (List<FollowedReference> followedReferences)
specifier|public
name|void
name|setFollowedReferences
parameter_list|(
name|List
argument_list|<
name|FollowedReference
argument_list|>
name|followedReferences
parameter_list|)
block|{
name|this
operator|.
name|followedReferences
operator|=
name|followedReferences
expr_stmt|;
block|}
DECL|method|getYmodules ()
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getYmodules
parameter_list|()
block|{
return|return
name|ymodules
return|;
block|}
DECL|method|setYmodules (List<String> ymodules)
specifier|public
name|void
name|setYmodules
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|ymodules
parameter_list|)
block|{
name|this
operator|.
name|ymodules
operator|=
name|ymodules
expr_stmt|;
block|}
DECL|method|getLikedMessageIds ()
specifier|public
name|List
argument_list|<
name|Long
argument_list|>
name|getLikedMessageIds
parameter_list|()
block|{
return|return
name|likedMessageIds
return|;
block|}
DECL|method|setLikedMessageIds (List<Long> likedMessageIds)
specifier|public
name|void
name|setLikedMessageIds
parameter_list|(
name|List
argument_list|<
name|Long
argument_list|>
name|likedMessageIds
parameter_list|)
block|{
name|this
operator|.
name|likedMessageIds
operator|=
name|likedMessageIds
expr_stmt|;
block|}
DECL|method|getFeedName ()
specifier|public
name|String
name|getFeedName
parameter_list|()
block|{
return|return
name|feedName
return|;
block|}
DECL|method|setFeedName (String feedName)
specifier|public
name|void
name|setFeedName
parameter_list|(
name|String
name|feedName
parameter_list|)
block|{
name|this
operator|.
name|feedName
operator|=
name|feedName
expr_stmt|;
block|}
DECL|method|getRealtime ()
specifier|public
name|Realtime
name|getRealtime
parameter_list|()
block|{
return|return
name|realtime
return|;
block|}
DECL|method|setRealtime (Realtime realtime)
specifier|public
name|void
name|setRealtime
parameter_list|(
name|Realtime
name|realtime
parameter_list|)
block|{
name|this
operator|.
name|realtime
operator|=
name|realtime
expr_stmt|;
block|}
DECL|method|getDirectFromBody ()
specifier|public
name|Boolean
name|getDirectFromBody
parameter_list|()
block|{
return|return
name|directFromBody
return|;
block|}
DECL|method|setDirectFromBody (Boolean directFromBody)
specifier|public
name|void
name|setDirectFromBody
parameter_list|(
name|Boolean
name|directFromBody
parameter_list|)
block|{
name|this
operator|.
name|directFromBody
operator|=
name|directFromBody
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"Meta [feedDesc="
operator|+
name|feedDesc
operator|+
literal|", currentUserId="
operator|+
name|currentUserId
operator|+
literal|", requestedPollInterval="
operator|+
name|requestedPollInterval
operator|+
literal|", olderAvailable="
operator|+
name|olderAvailable
operator|+
literal|", followedReferences="
operator|+
name|followedReferences
operator|+
literal|", ymodules="
operator|+
name|ymodules
operator|+
literal|", likedMessageIds="
operator|+
name|likedMessageIds
operator|+
literal|", feedName="
operator|+
name|feedName
operator|+
literal|", realtime="
operator|+
name|realtime
operator|+
literal|", directFromBody="
operator|+
name|directFromBody
operator|+
literal|"]"
return|;
block|}
block|}
end_class

end_unit

