begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|java
operator|.
name|io
operator|.
name|Serializable
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

begin_comment
comment|/**  * Represents an update with reference to the previous state.  */
end_comment

begin_class
annotation|@
name|JsonIgnoreProperties
argument_list|(
name|ignoreUnknown
operator|=
literal|true
argument_list|)
DECL|class|Update
specifier|public
class|class
name|Update
implements|implements
name|Serializable
block|{
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
operator|-
literal|4001092937174853655L
decl_stmt|;
annotation|@
name|JsonProperty
argument_list|(
literal|"update_id"
argument_list|)
DECL|field|updateId
specifier|private
name|Long
name|updateId
decl_stmt|;
DECL|field|message
specifier|private
name|IncomingMessage
name|message
decl_stmt|;
annotation|@
name|JsonProperty
argument_list|(
literal|"channel_post"
argument_list|)
DECL|field|channelpost
specifier|private
name|IncomingMessage
name|channelpost
decl_stmt|;
DECL|method|Update ()
specifier|public
name|Update
parameter_list|()
block|{     }
DECL|method|getUpdateId ()
specifier|public
name|Long
name|getUpdateId
parameter_list|()
block|{
return|return
name|updateId
return|;
block|}
DECL|method|setUpdateId (Long updateId)
specifier|public
name|void
name|setUpdateId
parameter_list|(
name|Long
name|updateId
parameter_list|)
block|{
name|this
operator|.
name|updateId
operator|=
name|updateId
expr_stmt|;
block|}
DECL|method|getMessage ()
specifier|public
name|IncomingMessage
name|getMessage
parameter_list|()
block|{
return|return
name|message
return|;
block|}
DECL|method|setMessage (IncomingMessage message)
specifier|public
name|void
name|setMessage
parameter_list|(
name|IncomingMessage
name|message
parameter_list|)
block|{
name|this
operator|.
name|message
operator|=
name|message
expr_stmt|;
block|}
DECL|method|getChannelPost ()
specifier|public
name|IncomingMessage
name|getChannelPost
parameter_list|()
block|{
return|return
name|channelpost
return|;
block|}
DECL|method|setChannelpost (IncomingMessage channelpost)
specifier|public
name|void
name|setChannelpost
parameter_list|(
name|IncomingMessage
name|channelpost
parameter_list|)
block|{
name|this
operator|.
name|channelpost
operator|=
name|channelpost
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
literal|"Update{"
argument_list|)
decl_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"updateId="
argument_list|)
operator|.
name|append
argument_list|(
name|updateId
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|", message="
argument_list|)
operator|.
name|append
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|", channel_post="
argument_list|)
operator|.
name|append
argument_list|(
name|channelpost
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

