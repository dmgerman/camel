begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.jmxconnect
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|jmxconnect
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|NotificationFilter
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|NotificationListener
import|;
end_import

begin_comment
comment|/**  * @version $Revision$  */
end_comment

begin_class
DECL|class|ListenerInfo
class|class
name|ListenerInfo
block|{
DECL|field|id
specifier|private
name|String
name|id
decl_stmt|;
DECL|field|listener
specifier|private
name|NotificationListener
name|listener
decl_stmt|;
DECL|field|filter
specifier|private
name|NotificationFilter
name|filter
decl_stmt|;
DECL|field|handback
specifier|private
name|Object
name|handback
decl_stmt|;
DECL|method|ListenerInfo (String id, NotificationListener listener, NotificationFilter filter, Object handback)
specifier|public
name|ListenerInfo
parameter_list|(
name|String
name|id
parameter_list|,
name|NotificationListener
name|listener
parameter_list|,
name|NotificationFilter
name|filter
parameter_list|,
name|Object
name|handback
parameter_list|)
block|{
name|this
operator|.
name|id
operator|=
name|id
expr_stmt|;
name|this
operator|.
name|listener
operator|=
name|listener
expr_stmt|;
name|this
operator|.
name|filter
operator|=
name|filter
expr_stmt|;
name|this
operator|.
name|handback
operator|=
name|handback
expr_stmt|;
block|}
comment|/**      * Is this info a match ?      *      * @param l      * @param f      * @param handback      * @return true if a match      */
DECL|method|isMatch (NotificationListener l, NotificationFilter f, Object handback)
specifier|public
name|boolean
name|isMatch
parameter_list|(
name|NotificationListener
name|l
parameter_list|,
name|NotificationFilter
name|f
parameter_list|,
name|Object
name|handback
parameter_list|)
block|{
return|return
name|listener
operator|==
name|listener
operator|&&
name|filter
operator|==
name|filter
operator|&&
name|handback
operator|==
name|handback
return|;
block|}
comment|/**      * @return Returns the filter.      */
DECL|method|getFilter ()
specifier|public
name|NotificationFilter
name|getFilter
parameter_list|()
block|{
return|return
name|filter
return|;
block|}
comment|/**      * @param filter The filter to set.      */
DECL|method|setFilter (NotificationFilter filter)
specifier|public
name|void
name|setFilter
parameter_list|(
name|NotificationFilter
name|filter
parameter_list|)
block|{
name|this
operator|.
name|filter
operator|=
name|filter
expr_stmt|;
block|}
comment|/**      * @return Returns the handback.      */
DECL|method|getHandback ()
specifier|public
name|Object
name|getHandback
parameter_list|()
block|{
return|return
name|handback
return|;
block|}
comment|/**      * @param handback The handback to set.      */
DECL|method|setHandback (Object handback)
specifier|public
name|void
name|setHandback
parameter_list|(
name|Object
name|handback
parameter_list|)
block|{
name|this
operator|.
name|handback
operator|=
name|handback
expr_stmt|;
block|}
comment|/**      * @return Returns the id.      */
DECL|method|getId ()
specifier|public
name|String
name|getId
parameter_list|()
block|{
return|return
name|id
return|;
block|}
comment|/**      * @param id The id to set.      */
DECL|method|setId (String id)
specifier|public
name|void
name|setId
parameter_list|(
name|String
name|id
parameter_list|)
block|{
name|this
operator|.
name|id
operator|=
name|id
expr_stmt|;
block|}
comment|/**      * @return Returns the listener.      */
DECL|method|getListener ()
specifier|public
name|NotificationListener
name|getListener
parameter_list|()
block|{
return|return
name|listener
return|;
block|}
comment|/**      * @param listener The listener to set.      */
DECL|method|setListener (NotificationListener listener)
specifier|public
name|void
name|setListener
parameter_list|(
name|NotificationListener
name|listener
parameter_list|)
block|{
name|this
operator|.
name|listener
operator|=
name|listener
expr_stmt|;
block|}
block|}
end_class

end_unit

