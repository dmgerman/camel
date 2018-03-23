begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.coap
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|coap
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|test
operator|.
name|AvailablePortFinder
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
name|test
operator|.
name|junit4
operator|.
name|CamelTestSupport
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
name|FileUtil
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|californium
operator|.
name|core
operator|.
name|CoapClient
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|californium
operator|.
name|core
operator|.
name|network
operator|.
name|config
operator|.
name|NetworkConfig
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
import|;
end_import

begin_class
DECL|class|CoAPTestSupport
specifier|public
class|class
name|CoAPTestSupport
extends|extends
name|CamelTestSupport
block|{
DECL|field|PORT
specifier|protected
specifier|static
specifier|final
name|int
name|PORT
init|=
name|AvailablePortFinder
operator|.
name|getNextAvailable
argument_list|()
decl_stmt|;
annotation|@
name|Before
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|NetworkConfig
operator|.
name|createStandardWithoutFile
argument_list|()
expr_stmt|;
block|}
DECL|method|createClient (String path)
specifier|protected
name|CoapClient
name|createClient
parameter_list|(
name|String
name|path
parameter_list|)
block|{
return|return
name|createClient
argument_list|(
name|path
argument_list|,
name|PORT
argument_list|)
return|;
block|}
DECL|method|createClient (String path, int port)
specifier|protected
name|CoapClient
name|createClient
parameter_list|(
name|String
name|path
parameter_list|,
name|int
name|port
parameter_list|)
block|{
name|String
name|url
init|=
name|String
operator|.
name|format
argument_list|(
literal|"coap://localhost:%d/%s"
argument_list|,
name|port
argument_list|,
name|FileUtil
operator|.
name|stripLeadingSeparator
argument_list|(
name|path
argument_list|)
argument_list|)
decl_stmt|;
return|return
operator|new
name|CoapClient
argument_list|(
name|url
argument_list|)
return|;
block|}
block|}
end_class

end_unit

