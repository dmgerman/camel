begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jmx
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jmx
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|Notification
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Processor
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
name|impl
operator|.
name|DefaultConsumer
import|;
end_import

begin_comment
comment|/**  * Creates an JMXExchange after getting a JMX Notification  *   * @version $Revision$  */
end_comment

begin_class
DECL|class|JMXConsumer
specifier|public
class|class
name|JMXConsumer
extends|extends
name|DefaultConsumer
implements|implements
name|NotificationListener
block|{
DECL|field|jmxEndpoint
specifier|private
name|JMXEndpoint
name|jmxEndpoint
decl_stmt|;
DECL|method|JMXConsumer (JMXEndpoint endpoint, Processor processor)
specifier|public
name|JMXConsumer
parameter_list|(
name|JMXEndpoint
name|endpoint
parameter_list|,
name|Processor
name|processor
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|,
name|processor
argument_list|)
expr_stmt|;
name|this
operator|.
name|jmxEndpoint
operator|=
name|endpoint
expr_stmt|;
block|}
DECL|method|handleNotification (Notification notification, Object handback)
specifier|public
name|void
name|handleNotification
parameter_list|(
name|Notification
name|notification
parameter_list|,
name|Object
name|handback
parameter_list|)
block|{
try|try
block|{
name|getProcessor
argument_list|()
operator|.
name|process
argument_list|(
name|jmxEndpoint
operator|.
name|createExchange
argument_list|(
name|notification
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|handleException
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

