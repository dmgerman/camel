begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.web.resources
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|web
operator|.
name|resources
package|;
end_package

begin_import
import|import
name|com
operator|.
name|sun
operator|.
name|jersey
operator|.
name|api
operator|.
name|client
operator|.
name|Client
import|;
end_import

begin_import
import|import
name|com
operator|.
name|sun
operator|.
name|jersey
operator|.
name|api
operator|.
name|client
operator|.
name|WebResource
import|;
end_import

begin_import
import|import
name|com
operator|.
name|sun
operator|.
name|jersey
operator|.
name|api
operator|.
name|client
operator|.
name|config
operator|.
name|ClientConfig
import|;
end_import

begin_import
import|import
name|com
operator|.
name|sun
operator|.
name|jersey
operator|.
name|api
operator|.
name|client
operator|.
name|config
operator|.
name|DefaultClientConfig
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
name|web
operator|.
name|Main
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
name|junit
operator|.
name|After
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Assert
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

begin_comment
comment|/**  * @version $Revision$  */
end_comment

begin_class
DECL|class|TestSupport
specifier|public
class|class
name|TestSupport
extends|extends
name|Assert
block|{
DECL|field|LOG
specifier|private
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
name|TestSupport
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|clientConfig
specifier|protected
name|ClientConfig
name|clientConfig
decl_stmt|;
DECL|field|client
specifier|protected
name|Client
name|client
decl_stmt|;
DECL|field|resource
specifier|protected
name|WebResource
name|resource
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
name|Main
operator|.
name|start
argument_list|()
expr_stmt|;
name|clientConfig
operator|=
operator|new
name|DefaultClientConfig
argument_list|()
expr_stmt|;
comment|// use the following jaxb context resolver
comment|//cc.getProviderClasses().add(JAXBContextResolver.class);
name|client
operator|=
name|Client
operator|.
name|create
argument_list|(
name|clientConfig
argument_list|)
expr_stmt|;
name|resource
operator|=
name|client
operator|.
name|resource
argument_list|(
name|Main
operator|.
name|getRootUrl
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|After
DECL|method|tearDown ()
specifier|public
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
name|Main
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
DECL|method|resource (String uri)
specifier|protected
name|WebResource
name|resource
parameter_list|(
name|String
name|uri
parameter_list|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"About to test URI: "
operator|+
name|uri
argument_list|)
expr_stmt|;
return|return
name|resource
operator|.
name|path
argument_list|(
name|uri
argument_list|)
return|;
block|}
DECL|method|assertHtmlResponse (String response)
specifier|protected
name|void
name|assertHtmlResponse
parameter_list|(
name|String
name|response
parameter_list|)
block|{
name|assertNotNull
argument_list|(
literal|"No text returned!"
argument_list|,
name|response
argument_list|)
expr_stmt|;
name|assertResponseContains
argument_list|(
name|response
argument_list|,
literal|"<html>"
argument_list|)
expr_stmt|;
name|assertResponseContains
argument_list|(
name|response
argument_list|,
literal|"</html>"
argument_list|)
expr_stmt|;
block|}
DECL|method|assertResponseContains (String response, String text)
specifier|protected
name|void
name|assertResponseContains
parameter_list|(
name|String
name|response
parameter_list|,
name|String
name|text
parameter_list|)
block|{
name|assertTrue
argument_list|(
literal|"Response should contain "
operator|+
name|text
operator|+
literal|" but was: "
operator|+
name|response
argument_list|,
name|response
operator|.
name|contains
argument_list|(
name|text
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

