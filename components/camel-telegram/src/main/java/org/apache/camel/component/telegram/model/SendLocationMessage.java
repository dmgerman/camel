begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.telegram.model
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|telegram
operator|.
name|model
package|;
end_package

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
DECL|class|SendLocationMessage
specifier|public
class|class
name|SendLocationMessage
extends|extends
name|OutgoingMessage
block|{
annotation|@
name|JsonProperty
argument_list|(
literal|"longitude"
argument_list|)
DECL|field|longitude
specifier|private
name|double
name|longitude
decl_stmt|;
annotation|@
name|JsonProperty
argument_list|(
literal|"latitude"
argument_list|)
DECL|field|latitude
specifier|private
name|double
name|latitude
decl_stmt|;
annotation|@
name|JsonProperty
argument_list|(
literal|"live_period"
argument_list|)
DECL|field|livePeriod
specifier|private
name|Integer
name|livePeriod
decl_stmt|;
annotation|@
name|JsonProperty
argument_list|(
literal|"reply_markup"
argument_list|)
DECL|field|replyKeyboardMarkup
specifier|private
name|ReplyKeyboardMarkup
name|replyKeyboardMarkup
decl_stmt|;
DECL|method|SendLocationMessage (double latitude, double longitude)
specifier|public
name|SendLocationMessage
parameter_list|(
name|double
name|latitude
parameter_list|,
name|double
name|longitude
parameter_list|)
block|{
name|this
operator|.
name|setLatitude
argument_list|(
name|latitude
argument_list|)
expr_stmt|;
name|this
operator|.
name|setLongitude
argument_list|(
name|longitude
argument_list|)
expr_stmt|;
block|}
DECL|method|setLatitude (double latitude)
specifier|public
name|void
name|setLatitude
parameter_list|(
name|double
name|latitude
parameter_list|)
block|{
name|this
operator|.
name|latitude
operator|=
name|latitude
expr_stmt|;
block|}
DECL|method|setLongitude (double longitude)
specifier|public
name|void
name|setLongitude
parameter_list|(
name|double
name|longitude
parameter_list|)
block|{
name|this
operator|.
name|longitude
operator|=
name|longitude
expr_stmt|;
block|}
DECL|method|setLivePeriod (Integer livePeriod)
specifier|public
name|void
name|setLivePeriod
parameter_list|(
name|Integer
name|livePeriod
parameter_list|)
block|{
name|this
operator|.
name|livePeriod
operator|=
name|livePeriod
expr_stmt|;
block|}
DECL|method|getReplyKeyboardMarkup ()
specifier|public
name|ReplyKeyboardMarkup
name|getReplyKeyboardMarkup
parameter_list|()
block|{
return|return
name|replyKeyboardMarkup
return|;
block|}
DECL|method|setReplyKeyboardMarkup (ReplyKeyboardMarkup replyKeyboardMarkup)
specifier|public
name|void
name|setReplyKeyboardMarkup
parameter_list|(
name|ReplyKeyboardMarkup
name|replyKeyboardMarkup
parameter_list|)
block|{
name|this
operator|.
name|replyKeyboardMarkup
operator|=
name|replyKeyboardMarkup
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
specifier|final
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|(
literal|"SendLocationMessage{"
argument_list|)
decl_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"latitude="
argument_list|)
operator|.
name|append
argument_list|(
name|latitude
argument_list|)
operator|.
name|append
argument_list|(
literal|'\''
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|", longitude="
argument_list|)
operator|.
name|append
argument_list|(
name|longitude
argument_list|)
operator|.
name|append
argument_list|(
literal|'\''
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|", livePeriod="
argument_list|)
operator|.
name|append
argument_list|(
name|livePeriod
argument_list|)
operator|.
name|append
argument_list|(
literal|'\''
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|", disableNotification="
argument_list|)
operator|.
name|append
argument_list|(
name|disableNotification
argument_list|)
operator|.
name|append
argument_list|(
literal|'\''
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|", replyToMessageId="
argument_list|)
operator|.
name|append
argument_list|(
name|replyToMessageId
argument_list|)
operator|.
name|append
argument_list|(
literal|'\''
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|", replyKeyboardMarkup="
argument_list|)
operator|.
name|append
argument_list|(
name|replyKeyboardMarkup
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|'}'
argument_list|)
expr_stmt|;
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

