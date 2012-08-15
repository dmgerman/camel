begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.sjms.jms
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|sjms
operator|.
name|jms
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|Connection
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|ConnectionFactory
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
name|util
operator|.
name|ObjectHelper
import|;
end_import

begin_comment
comment|/**  * The default {@link ConnectionResource} implementation for the SJMSComponent.  */
end_comment

begin_class
DECL|class|ConnectionFactoryResource
specifier|public
class|class
name|ConnectionFactoryResource
extends|extends
name|ObjectPool
argument_list|<
name|Connection
argument_list|>
implements|implements
name|ConnectionResource
block|{
DECL|field|connectionFactory
specifier|private
name|ConnectionFactory
name|connectionFactory
decl_stmt|;
DECL|field|username
specifier|private
name|String
name|username
decl_stmt|;
DECL|field|password
specifier|private
name|String
name|password
decl_stmt|;
DECL|field|clientId
specifier|private
name|String
name|clientId
decl_stmt|;
comment|/**      * Default Constructor      */
DECL|method|ConnectionFactoryResource ()
specifier|public
name|ConnectionFactoryResource
parameter_list|()
block|{     }
comment|/**      * TODO Add Constructor Javadoc      *       * @param poolSize      * @param connectionFactory      */
DECL|method|ConnectionFactoryResource (int poolSize, ConnectionFactory connectionFactory)
specifier|public
name|ConnectionFactoryResource
parameter_list|(
name|int
name|poolSize
parameter_list|,
name|ConnectionFactory
name|connectionFactory
parameter_list|)
block|{
name|this
argument_list|(
name|poolSize
argument_list|,
name|connectionFactory
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
comment|/**      * @param poolSize      * @param connectionFactory      * @param username      * @param password      */
DECL|method|ConnectionFactoryResource (int poolSize, ConnectionFactory connectionFactory, String username, String password)
specifier|public
name|ConnectionFactoryResource
parameter_list|(
name|int
name|poolSize
parameter_list|,
name|ConnectionFactory
name|connectionFactory
parameter_list|,
name|String
name|username
parameter_list|,
name|String
name|password
parameter_list|)
block|{
name|super
argument_list|(
name|poolSize
argument_list|)
expr_stmt|;
name|this
operator|.
name|connectionFactory
operator|=
name|connectionFactory
expr_stmt|;
name|this
operator|.
name|username
operator|=
name|username
expr_stmt|;
name|this
operator|.
name|password
operator|=
name|password
expr_stmt|;
block|}
comment|/**      * @param poolSize      * @param connectionFactory      * @param username      * @param password      */
DECL|method|ConnectionFactoryResource (int poolSize, ConnectionFactory connectionFactory, String username, String password, String connectionId)
specifier|public
name|ConnectionFactoryResource
parameter_list|(
name|int
name|poolSize
parameter_list|,
name|ConnectionFactory
name|connectionFactory
parameter_list|,
name|String
name|username
parameter_list|,
name|String
name|password
parameter_list|,
name|String
name|connectionId
parameter_list|)
block|{
name|super
argument_list|(
name|poolSize
argument_list|)
expr_stmt|;
name|this
operator|.
name|connectionFactory
operator|=
name|connectionFactory
expr_stmt|;
name|this
operator|.
name|username
operator|=
name|username
expr_stmt|;
name|this
operator|.
name|password
operator|=
name|password
expr_stmt|;
name|this
operator|.
name|clientId
operator|=
name|connectionId
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|borrowConnection ()
specifier|public
name|Connection
name|borrowConnection
parameter_list|()
throws|throws
name|Exception
block|{
return|return
name|this
operator|.
name|borrowObject
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|borrowConnection (long timeout)
specifier|public
name|Connection
name|borrowConnection
parameter_list|(
name|long
name|timeout
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|this
operator|.
name|borrowObject
argument_list|(
name|timeout
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|returnConnection (Connection connection)
specifier|public
name|void
name|returnConnection
parameter_list|(
name|Connection
name|connection
parameter_list|)
throws|throws
name|Exception
block|{
name|returnObject
argument_list|(
name|connection
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createObject ()
specifier|protected
name|Connection
name|createObject
parameter_list|()
throws|throws
name|Exception
block|{
name|Connection
name|connection
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|connectionFactory
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|getUsername
argument_list|()
operator|!=
literal|null
operator|&&
name|getPassword
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|connection
operator|=
name|connectionFactory
operator|.
name|createConnection
argument_list|(
name|getUsername
argument_list|()
argument_list|,
name|getPassword
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|connection
operator|=
name|connectionFactory
operator|.
name|createConnection
argument_list|()
expr_stmt|;
block|}
block|}
if|if
condition|(
name|connection
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|getClientId
argument_list|()
argument_list|)
condition|)
block|{
name|connection
operator|.
name|setClientID
argument_list|(
name|getClientId
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|connection
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
return|return
name|connection
return|;
block|}
annotation|@
name|Override
DECL|method|destroyObject (Connection connection)
specifier|protected
name|void
name|destroyObject
parameter_list|(
name|Connection
name|connection
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|connection
operator|!=
literal|null
condition|)
block|{
name|connection
operator|.
name|stop
argument_list|()
expr_stmt|;
name|connection
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|getConnectionFactory ()
specifier|public
name|ConnectionFactory
name|getConnectionFactory
parameter_list|()
block|{
return|return
name|connectionFactory
return|;
block|}
DECL|method|setConnectionFactory (ConnectionFactory connectionFactory)
specifier|public
name|void
name|setConnectionFactory
parameter_list|(
name|ConnectionFactory
name|connectionFactory
parameter_list|)
block|{
name|this
operator|.
name|connectionFactory
operator|=
name|connectionFactory
expr_stmt|;
block|}
DECL|method|getUsername ()
specifier|public
name|String
name|getUsername
parameter_list|()
block|{
return|return
name|username
return|;
block|}
DECL|method|setUsername (String username)
specifier|public
name|void
name|setUsername
parameter_list|(
name|String
name|username
parameter_list|)
block|{
name|this
operator|.
name|username
operator|=
name|username
expr_stmt|;
block|}
DECL|method|getPassword ()
specifier|public
name|String
name|getPassword
parameter_list|()
block|{
return|return
name|password
return|;
block|}
DECL|method|setPassword (String password)
specifier|public
name|void
name|setPassword
parameter_list|(
name|String
name|password
parameter_list|)
block|{
name|this
operator|.
name|password
operator|=
name|password
expr_stmt|;
block|}
DECL|method|getClientId ()
specifier|public
name|String
name|getClientId
parameter_list|()
block|{
return|return
name|clientId
return|;
block|}
DECL|method|setClientId (String clientId)
specifier|public
name|void
name|setClientId
parameter_list|(
name|String
name|clientId
parameter_list|)
block|{
name|this
operator|.
name|clientId
operator|=
name|clientId
expr_stmt|;
block|}
block|}
end_class

end_unit

