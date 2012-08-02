begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.sjms.pool
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
name|pool
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
name|JMSException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|Session
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|XASession
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|transaction
operator|.
name|xa
operator|.
name|XAResource
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
name|component
operator|.
name|sjms
operator|.
name|ConnectionResource
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
name|component
operator|.
name|sjms
operator|.
name|jms
operator|.
name|SessionAcknowledgementType
import|;
end_import

begin_comment
comment|/**  * TODO Add Class documentation for SessionPool  *   */
end_comment

begin_class
DECL|class|SessionPool
specifier|public
class|class
name|SessionPool
extends|extends
name|ObjectPool
argument_list|<
name|Session
argument_list|>
block|{
DECL|field|connectionResource
specifier|private
name|ConnectionResource
name|connectionResource
decl_stmt|;
DECL|field|transacted
specifier|private
name|boolean
name|transacted
decl_stmt|;
DECL|field|acknowledgeMode
specifier|private
name|SessionAcknowledgementType
name|acknowledgeMode
init|=
name|SessionAcknowledgementType
operator|.
name|AUTO_ACKNOWLEDGE
decl_stmt|;
comment|/**      * TODO Add Constructor Javadoc      *      */
DECL|method|SessionPool (int poolSize, ConnectionResource connectionResource)
specifier|public
name|SessionPool
parameter_list|(
name|int
name|poolSize
parameter_list|,
name|ConnectionResource
name|connectionResource
parameter_list|)
block|{
name|super
argument_list|(
name|poolSize
argument_list|)
expr_stmt|;
name|this
operator|.
name|connectionResource
operator|=
name|connectionResource
expr_stmt|;
block|}
comment|/**      * TODO Add Constructor Javadoc      *      * @param poolSize      */
DECL|method|SessionPool (int poolSize)
specifier|public
name|SessionPool
parameter_list|(
name|int
name|poolSize
parameter_list|)
block|{
name|super
argument_list|(
name|poolSize
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createObject ()
specifier|protected
name|Session
name|createObject
parameter_list|()
throws|throws
name|Exception
block|{
name|Session
name|session
init|=
literal|null
decl_stmt|;
specifier|final
name|Connection
name|connection
init|=
name|getConnectionResource
argument_list|()
operator|.
name|borrowConnection
argument_list|(
literal|5000
argument_list|)
decl_stmt|;
if|if
condition|(
name|connection
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|transacted
condition|)
block|{
name|session
operator|=
name|connection
operator|.
name|createSession
argument_list|(
name|transacted
argument_list|,
name|Session
operator|.
name|AUTO_ACKNOWLEDGE
argument_list|)
expr_stmt|;
block|}
else|else
block|{
switch|switch
condition|(
name|acknowledgeMode
condition|)
block|{
case|case
name|CLIENT_ACKNOWLEDGE
case|:
name|session
operator|=
name|connection
operator|.
name|createSession
argument_list|(
name|transacted
argument_list|,
name|Session
operator|.
name|CLIENT_ACKNOWLEDGE
argument_list|)
expr_stmt|;
break|break;
case|case
name|DUPS_OK_ACKNOWLEDGE
case|:
name|session
operator|=
name|connection
operator|.
name|createSession
argument_list|(
name|transacted
argument_list|,
name|Session
operator|.
name|DUPS_OK_ACKNOWLEDGE
argument_list|)
expr_stmt|;
break|break;
case|case
name|AUTO_ACKNOWLEDGE
case|:
name|session
operator|=
name|connection
operator|.
name|createSession
argument_list|(
name|transacted
argument_list|,
name|Session
operator|.
name|AUTO_ACKNOWLEDGE
argument_list|)
expr_stmt|;
break|break;
default|default:
comment|// do nothing here.
block|}
block|}
block|}
name|getConnectionResource
argument_list|()
operator|.
name|returnConnection
argument_list|(
name|connection
argument_list|)
expr_stmt|;
return|return
name|session
return|;
block|}
annotation|@
name|Override
DECL|method|destroyObject (Session session)
specifier|protected
name|void
name|destroyObject
parameter_list|(
name|Session
name|session
parameter_list|)
throws|throws
name|Exception
block|{
comment|// lets reset the session
name|session
operator|.
name|setMessageListener
argument_list|(
literal|null
argument_list|)
expr_stmt|;
if|if
condition|(
name|transacted
condition|)
block|{
try|try
block|{
name|session
operator|.
name|rollback
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|JMSException
name|e
parameter_list|)
block|{
name|logger
operator|.
name|warn
argument_list|(
literal|"Caught exception trying rollback() when putting session back into the pool, will invalidate. "
operator|+
name|e
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|session
operator|!=
literal|null
condition|)
block|{
name|session
operator|.
name|close
argument_list|()
expr_stmt|;
name|session
operator|=
literal|null
expr_stmt|;
block|}
block|}
comment|/**      * Gets the SessionAcknowledgementType value of acknowledgeMode for this instance of SessionPool.      *      * @return the DEFAULT_ACKNOWLEDGE_MODE      */
DECL|method|getAcknowledgeMode ()
specifier|public
specifier|final
name|SessionAcknowledgementType
name|getAcknowledgeMode
parameter_list|()
block|{
return|return
name|acknowledgeMode
return|;
block|}
comment|/**      * Sets the SessionAcknowledgementType value of acknowledgeMode for this instance of SessionPool.      *      * @param acknowledgeMode Sets SessionAcknowledgementType, default is AUTO_ACKNOWLEDGE      */
DECL|method|setAcknowledgeMode (SessionAcknowledgementType acknowledgeMode)
specifier|public
specifier|final
name|void
name|setAcknowledgeMode
parameter_list|(
name|SessionAcknowledgementType
name|acknowledgeMode
parameter_list|)
block|{
name|this
operator|.
name|acknowledgeMode
operator|=
name|acknowledgeMode
expr_stmt|;
block|}
comment|/**      * Gets the boolean value of transacted for this instance of SessionPool.      *      * @return the transacted      */
DECL|method|isTransacted ()
specifier|public
specifier|final
name|boolean
name|isTransacted
parameter_list|()
block|{
return|return
name|transacted
return|;
block|}
comment|/**      * Sets the boolean value of transacted for this instance of SessionPool.      *      * @param transacted Sets boolean, default is TODO add default      */
DECL|method|setTransacted (boolean transacted)
specifier|public
specifier|final
name|void
name|setTransacted
parameter_list|(
name|boolean
name|transacted
parameter_list|)
block|{
name|this
operator|.
name|transacted
operator|=
name|transacted
expr_stmt|;
block|}
comment|/**      * Gets the DefaultConnectionResource value of connectionResource for this instance of SessionPool.      *      * @return the connectionResource      */
DECL|method|getConnectionResource ()
specifier|public
name|ConnectionResource
name|getConnectionResource
parameter_list|()
block|{
return|return
name|connectionResource
return|;
block|}
DECL|method|createXaResource (XASession session)
specifier|protected
name|XAResource
name|createXaResource
parameter_list|(
name|XASession
name|session
parameter_list|)
throws|throws
name|JMSException
block|{
return|return
name|session
operator|.
name|getXAResource
argument_list|()
return|;
block|}
comment|//    protected class Synchronization implements javax.transaction.Synchronization {
comment|//        private final XASession session;
comment|//
comment|//        private Synchronization(XASession session) {
comment|//            this.session = session;
comment|//        }
comment|//
comment|//        public void beforeCompletion() {
comment|//        }
comment|//
comment|//        public void afterCompletion(int status) {
comment|//            try {
comment|//                // This will return session to the pool.
comment|//                session.setIgnoreClose(false);
comment|//                session.close();
comment|//                session.setIsXa(false);
comment|//            } catch (JMSException e) {
comment|//                throw new RuntimeException(e);
comment|//            }
comment|//        }
comment|//    }
block|}
end_class

end_unit

