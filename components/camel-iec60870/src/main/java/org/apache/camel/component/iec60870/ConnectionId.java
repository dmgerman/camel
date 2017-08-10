begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.iec60870
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|iec60870
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Objects
import|;
end_import

begin_class
DECL|class|ConnectionId
specifier|public
class|class
name|ConnectionId
block|{
DECL|field|host
specifier|private
specifier|final
name|String
name|host
decl_stmt|;
DECL|field|port
specifier|private
specifier|final
name|int
name|port
decl_stmt|;
DECL|field|connectionId
specifier|private
specifier|final
name|String
name|connectionId
decl_stmt|;
DECL|method|ConnectionId (final String host, final int port, final String connectionId)
specifier|public
name|ConnectionId
parameter_list|(
specifier|final
name|String
name|host
parameter_list|,
specifier|final
name|int
name|port
parameter_list|,
specifier|final
name|String
name|connectionId
parameter_list|)
block|{
name|Objects
operator|.
name|requireNonNull
argument_list|(
name|host
argument_list|)
expr_stmt|;
if|if
condition|(
name|port
operator|<=
literal|0
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Port must be greater than 0"
argument_list|)
throw|;
block|}
name|this
operator|.
name|host
operator|=
name|host
expr_stmt|;
name|this
operator|.
name|port
operator|=
name|port
expr_stmt|;
name|this
operator|.
name|connectionId
operator|=
name|connectionId
expr_stmt|;
block|}
DECL|method|getHost ()
specifier|public
name|String
name|getHost
parameter_list|()
block|{
return|return
name|this
operator|.
name|host
return|;
block|}
DECL|method|getPort ()
specifier|public
name|int
name|getPort
parameter_list|()
block|{
return|return
name|this
operator|.
name|port
return|;
block|}
DECL|method|getConnectionId ()
specifier|public
name|String
name|getConnectionId
parameter_list|()
block|{
return|return
name|this
operator|.
name|connectionId
return|;
block|}
annotation|@
name|Override
DECL|method|hashCode ()
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
specifier|final
name|int
name|prime
init|=
literal|31
decl_stmt|;
name|int
name|result
init|=
literal|1
decl_stmt|;
name|result
operator|=
name|prime
operator|*
name|result
operator|+
operator|(
name|this
operator|.
name|connectionId
operator|==
literal|null
condition|?
literal|0
else|:
name|this
operator|.
name|connectionId
operator|.
name|hashCode
argument_list|()
operator|)
expr_stmt|;
name|result
operator|=
name|prime
operator|*
name|result
operator|+
operator|(
name|this
operator|.
name|host
operator|==
literal|null
condition|?
literal|0
else|:
name|this
operator|.
name|host
operator|.
name|hashCode
argument_list|()
operator|)
expr_stmt|;
name|result
operator|=
name|prime
operator|*
name|result
operator|+
name|this
operator|.
name|port
expr_stmt|;
return|return
name|result
return|;
block|}
annotation|@
name|Override
DECL|method|equals (final Object obj)
specifier|public
name|boolean
name|equals
parameter_list|(
specifier|final
name|Object
name|obj
parameter_list|)
block|{
if|if
condition|(
name|this
operator|==
name|obj
condition|)
block|{
return|return
literal|true
return|;
block|}
if|if
condition|(
name|obj
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|getClass
argument_list|()
operator|!=
name|obj
operator|.
name|getClass
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
specifier|final
name|ConnectionId
name|other
init|=
operator|(
name|ConnectionId
operator|)
name|obj
decl_stmt|;
if|if
condition|(
name|this
operator|.
name|connectionId
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|other
operator|.
name|connectionId
operator|!=
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
elseif|else
if|if
condition|(
operator|!
name|this
operator|.
name|connectionId
operator|.
name|equals
argument_list|(
name|other
operator|.
name|connectionId
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|this
operator|.
name|host
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|other
operator|.
name|host
operator|!=
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
elseif|else
if|if
condition|(
operator|!
name|this
operator|.
name|host
operator|.
name|equals
argument_list|(
name|other
operator|.
name|host
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|this
operator|.
name|port
operator|!=
name|other
operator|.
name|port
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
literal|true
return|;
block|}
block|}
end_class

end_unit

