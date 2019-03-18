begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.ahc.ws
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|ahc
operator|.
name|ws
package|;
end_package

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jetty
operator|.
name|server
operator|.
name|Connector
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jetty
operator|.
name|server
operator|.
name|ServerConnector
import|;
end_import

begin_class
DECL|class|WsProducerTest
specifier|public
class|class
name|WsProducerTest
extends|extends
name|WsProducerTestBase
block|{
annotation|@
name|Override
DECL|method|setUpComponent ()
specifier|protected
name|void
name|setUpComponent
parameter_list|()
throws|throws
name|Exception
block|{     }
annotation|@
name|Override
DECL|method|getConnector ()
specifier|protected
name|Connector
name|getConnector
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|ServerConnector
argument_list|(
name|server
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|getTargetURL ()
specifier|protected
name|String
name|getTargetURL
parameter_list|()
block|{
return|return
literal|"ahc-ws://localhost:"
operator|+
name|PORT
return|;
block|}
block|}
end_class

end_unit

