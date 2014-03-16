begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jcr
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jcr
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileNotFoundException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jcr
operator|.
name|Repository
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jcr
operator|.
name|RepositoryException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jcr
operator|.
name|Session
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jcr
operator|.
name|SimpleCredentials
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|naming
operator|.
name|Context
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
name|jackrabbit
operator|.
name|core
operator|.
name|TransientRepository
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
comment|/**  * JcrRouteTestSupport  *   * @version $Id$  */
end_comment

begin_class
DECL|class|JcrRouteTestSupport
specifier|public
specifier|abstract
class|class
name|JcrRouteTestSupport
extends|extends
name|CamelTestSupport
block|{
DECL|field|CONFIG_FILE
specifier|protected
specifier|static
specifier|final
name|String
name|CONFIG_FILE
init|=
literal|"target/test-classes/repository-simple-security.xml"
decl_stmt|;
DECL|field|REPO_PATH
specifier|protected
specifier|static
specifier|final
name|String
name|REPO_PATH
init|=
literal|"target/repository-simple-security"
decl_stmt|;
DECL|field|repository
specifier|private
name|Repository
name|repository
decl_stmt|;
annotation|@
name|Override
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
name|deleteDirectory
argument_list|(
name|REPO_PATH
argument_list|)
expr_stmt|;
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
block|}
DECL|method|getRepository ()
specifier|protected
name|Repository
name|getRepository
parameter_list|()
block|{
return|return
name|repository
return|;
block|}
DECL|method|openSession ()
specifier|protected
name|Session
name|openSession
parameter_list|()
throws|throws
name|RepositoryException
block|{
return|return
name|getRepository
argument_list|()
operator|.
name|login
argument_list|(
operator|new
name|SimpleCredentials
argument_list|(
literal|"user"
argument_list|,
literal|"pass"
operator|.
name|toCharArray
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|createJndiContext ()
specifier|protected
name|Context
name|createJndiContext
parameter_list|()
throws|throws
name|Exception
block|{
name|File
name|config
init|=
operator|new
name|File
argument_list|(
name|CONFIG_FILE
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|config
operator|.
name|exists
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|FileNotFoundException
argument_list|(
literal|"Missing config file: "
operator|+
name|config
operator|.
name|getPath
argument_list|()
argument_list|)
throw|;
block|}
name|Context
name|context
init|=
name|super
operator|.
name|createJndiContext
argument_list|()
decl_stmt|;
name|repository
operator|=
operator|new
name|TransientRepository
argument_list|(
name|CONFIG_FILE
argument_list|,
name|REPO_PATH
argument_list|)
expr_stmt|;
name|context
operator|.
name|bind
argument_list|(
literal|"repository"
argument_list|,
name|repository
argument_list|)
expr_stmt|;
return|return
name|context
return|;
block|}
block|}
end_class

end_unit

