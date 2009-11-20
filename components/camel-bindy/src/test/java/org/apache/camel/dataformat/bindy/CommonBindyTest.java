begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.dataformat.bindy
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|dataformat
operator|.
name|bindy
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
name|EndpointInject
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
name|Produce
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
name|ProducerTemplate
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
name|mock
operator|.
name|MockEndpoint
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|Log
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|LogFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|test
operator|.
name|context
operator|.
name|junit4
operator|.
name|AbstractJUnit4SpringContextTests
import|;
end_import

begin_class
DECL|class|CommonBindyTest
specifier|public
class|class
name|CommonBindyTest
extends|extends
name|AbstractJUnit4SpringContextTests
block|{
DECL|field|LOG
specifier|protected
specifier|static
specifier|final
specifier|transient
name|Log
name|LOG
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|CommonBindyTest
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|URI_MOCK_RESULT
specifier|public
specifier|static
specifier|final
name|String
name|URI_MOCK_RESULT
init|=
literal|"mock:result"
decl_stmt|;
DECL|field|URI_MOCK_ERROR
specifier|public
specifier|static
specifier|final
name|String
name|URI_MOCK_ERROR
init|=
literal|"mock:error"
decl_stmt|;
DECL|field|URI_DIRECT_START
specifier|public
specifier|static
specifier|final
name|String
name|URI_DIRECT_START
init|=
literal|"direct:start"
decl_stmt|;
DECL|field|URI_FILE_FIX
specifier|public
specifier|static
specifier|final
name|String
name|URI_FILE_FIX
init|=
literal|"file://src/test/data/fix?noop=true"
decl_stmt|;
DECL|field|URI_FILE_FIX_TAB
specifier|public
specifier|static
specifier|final
name|String
name|URI_FILE_FIX_TAB
init|=
literal|"file://src/test/data/fix_tab?noop=true"
decl_stmt|;
annotation|@
name|Produce
argument_list|(
name|uri
operator|=
name|URI_DIRECT_START
argument_list|)
DECL|field|template
specifier|public
name|ProducerTemplate
name|template
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
name|uri
operator|=
name|URI_MOCK_RESULT
argument_list|)
DECL|field|result
specifier|public
name|MockEndpoint
name|result
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
name|uri
operator|=
name|URI_MOCK_ERROR
argument_list|)
DECL|field|error
specifier|public
name|MockEndpoint
name|error
decl_stmt|;
block|}
end_class

end_unit

