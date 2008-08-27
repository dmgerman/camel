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

begin_comment
comment|/**  * @version $Revision$  */
end_comment

begin_class
DECL|class|JmxActiveMQRemoteTest
specifier|public
class|class
name|JmxActiveMQRemoteTest
extends|extends
name|JmxRemoteTest
block|{
annotation|@
name|Override
DECL|method|setUp ()
specifier|protected
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|brokerUrl
operator|=
literal|"broker:(tcp://localhost:6000)/localhost?persistent=false"
expr_stmt|;
name|clientServiceUrl
operator|=
literal|"service:jmx:activemq:///tcp://localhost:6000"
expr_stmt|;
name|serverServiceUrl
operator|=
name|clientServiceUrl
expr_stmt|;
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

