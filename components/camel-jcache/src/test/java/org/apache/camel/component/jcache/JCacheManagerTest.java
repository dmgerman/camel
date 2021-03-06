begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jcache
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jcache
package|;
end_package

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_class
DECL|class|JCacheManagerTest
specifier|public
class|class
name|JCacheManagerTest
extends|extends
name|JCacheComponentTestSupport
block|{
annotation|@
name|Test
DECL|method|testCacheCreation ()
specifier|public
name|void
name|testCacheCreation
parameter_list|()
throws|throws
name|Exception
block|{
name|JCacheConfiguration
name|conf
init|=
operator|new
name|JCacheConfiguration
argument_list|()
decl_stmt|;
name|conf
operator|.
name|setCacheName
argument_list|(
name|randomString
argument_list|()
argument_list|)
expr_stmt|;
name|JCacheManager
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|manager
init|=
operator|new
name|JCacheManager
argument_list|<>
argument_list|(
name|conf
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|manager
operator|.
name|getCache
argument_list|()
argument_list|)
expr_stmt|;
name|manager
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|IllegalStateException
operator|.
name|class
argument_list|)
DECL|method|testCacheCreationFailure ()
specifier|public
name|void
name|testCacheCreationFailure
parameter_list|()
throws|throws
name|Exception
block|{
name|JCacheConfiguration
name|conf
init|=
operator|new
name|JCacheConfiguration
argument_list|()
decl_stmt|;
name|conf
operator|.
name|setCacheName
argument_list|(
name|randomString
argument_list|()
argument_list|)
expr_stmt|;
name|conf
operator|.
name|setCreateCacheIfNotExists
argument_list|(
literal|false
argument_list|)
expr_stmt|;
operator|new
name|JCacheManager
argument_list|<>
argument_list|(
name|conf
argument_list|)
operator|.
name|getCache
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"Should have raised IllegalStateException"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

