begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jt400
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jt400
package|;
end_package

begin_import
import|import
name|com
operator|.
name|ibm
operator|.
name|as400
operator|.
name|access
operator|.
name|AS400
import|;
end_import

begin_import
import|import
name|com
operator|.
name|ibm
operator|.
name|as400
operator|.
name|access
operator|.
name|BaseDataQueue
import|;
end_import

begin_import
import|import
name|com
operator|.
name|ibm
operator|.
name|as400
operator|.
name|access
operator|.
name|DataQueue
import|;
end_import

begin_import
import|import
name|com
operator|.
name|ibm
operator|.
name|as400
operator|.
name|access
operator|.
name|KeyedDataQueue
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
name|RuntimeCamelException
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
name|Service
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

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_comment
comment|/**  * Pseudo-abstract class that encapsulates Service logic common to  * {@link Jt400DataQueueConsumer} and {@link Jt400DataQueueProducer}.  */
end_comment

begin_class
DECL|class|Jt400DataQueueService
class|class
name|Jt400DataQueueService
implements|implements
name|Service
block|{
comment|/**      * Logging tool.      */
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|Jt400DataQueueService
operator|.
name|class
argument_list|)
decl_stmt|;
comment|/**      * Endpoint which this service connects to.      */
DECL|field|endpoint
specifier|private
specifier|final
name|Jt400Endpoint
name|endpoint
decl_stmt|;
comment|/**      * Data queue object that corresponds to the endpoint of this service (null if stopped).      */
DECL|field|queue
specifier|private
name|BaseDataQueue
name|queue
decl_stmt|;
comment|/**      * Creates a {@code Jt400DataQueueService} that connects to the specified      * endpoint.      *       * @param endpoint endpoint which this service connects to      */
DECL|method|Jt400DataQueueService (Jt400Endpoint endpoint)
name|Jt400DataQueueService
parameter_list|(
name|Jt400Endpoint
name|endpoint
parameter_list|)
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|endpoint
argument_list|,
literal|"endpoint"
argument_list|,
name|this
argument_list|)
expr_stmt|;
name|this
operator|.
name|endpoint
operator|=
name|endpoint
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|start ()
specifier|public
name|void
name|start
parameter_list|()
block|{
if|if
condition|(
name|queue
operator|==
literal|null
condition|)
block|{
name|AS400
name|system
init|=
name|endpoint
operator|.
name|getSystem
argument_list|()
decl_stmt|;
if|if
condition|(
name|endpoint
operator|.
name|isKeyed
argument_list|()
condition|)
block|{
name|queue
operator|=
operator|new
name|KeyedDataQueue
argument_list|(
name|system
argument_list|,
name|endpoint
operator|.
name|getObjectPath
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|queue
operator|=
operator|new
name|DataQueue
argument_list|(
name|system
argument_list|,
name|endpoint
operator|.
name|getObjectPath
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
operator|!
name|queue
operator|.
name|getSystem
argument_list|()
operator|.
name|isConnected
argument_list|(
name|AS400
operator|.
name|DATAQUEUE
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Connecting to {}"
argument_list|,
name|endpoint
argument_list|)
expr_stmt|;
try|try
block|{
name|queue
operator|.
name|getSystem
argument_list|()
operator|.
name|connectService
argument_list|(
name|AS400
operator|.
name|DATAQUEUE
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
name|RuntimeCamelException
operator|.
name|wrapRuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
block|}
annotation|@
name|Override
DECL|method|stop ()
specifier|public
name|void
name|stop
parameter_list|()
block|{
if|if
condition|(
name|queue
operator|!=
literal|null
condition|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Releasing connection to {}"
argument_list|,
name|endpoint
argument_list|)
expr_stmt|;
name|AS400
name|system
init|=
name|queue
operator|.
name|getSystem
argument_list|()
decl_stmt|;
name|queue
operator|=
literal|null
expr_stmt|;
name|endpoint
operator|.
name|releaseSystem
argument_list|(
name|system
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Returns the data queue object that this service connects to. Returns      * {@code null} if the service is stopped.      *       * @return the data queue object that this service connects to, or      *         {@code null} if stopped      */
DECL|method|getDataQueue ()
specifier|public
name|BaseDataQueue
name|getDataQueue
parameter_list|()
block|{
return|return
name|queue
return|;
block|}
block|}
end_class

end_unit

