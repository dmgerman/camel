begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cache
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|cache
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
import|;
end_import

begin_import
import|import
name|net
operator|.
name|sf
operator|.
name|ehcache
operator|.
name|config
operator|.
name|Configuration
import|;
end_import

begin_import
import|import
name|net
operator|.
name|sf
operator|.
name|ehcache
operator|.
name|config
operator|.
name|ConfigurationFactory
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
name|Test
import|;
end_import

begin_comment
comment|/**  *   */
end_comment

begin_class
DECL|class|EHCacheUtilTest
specifier|public
class|class
name|EHCacheUtilTest
extends|extends
name|Assert
block|{
annotation|@
name|Test
DECL|method|testCreateCacheManagers ()
specifier|public
name|void
name|testCreateCacheManagers
parameter_list|()
throws|throws
name|Exception
block|{
comment|// no arg
name|assertNotNull
argument_list|(
literal|"create with no arg"
argument_list|,
name|EHCacheUtil
operator|.
name|createCacheManager
argument_list|()
argument_list|)
expr_stmt|;
name|URL
name|configURL
init|=
name|EHCacheUtil
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"/test-ehcache.xml"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|configURL
argument_list|)
expr_stmt|;
comment|// string
name|assertNotNull
argument_list|(
literal|"create with string"
argument_list|,
name|EHCacheUtil
operator|.
name|createCacheManager
argument_list|(
name|configURL
operator|.
name|getPath
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
comment|// url
name|assertNotNull
argument_list|(
literal|"create with url"
argument_list|,
name|EHCacheUtil
operator|.
name|createCacheManager
argument_list|(
name|configURL
argument_list|)
argument_list|)
expr_stmt|;
comment|// inputstream
name|assertNotNull
argument_list|(
literal|"create with inputstream"
argument_list|,
name|EHCacheUtil
operator|.
name|createCacheManager
argument_list|(
name|configURL
operator|.
name|openStream
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
comment|// config
name|Configuration
name|conf
init|=
name|ConfigurationFactory
operator|.
name|parseConfiguration
argument_list|(
name|configURL
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|conf
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"create with configuration"
argument_list|,
name|EHCacheUtil
operator|.
name|createCacheManager
argument_list|(
name|conf
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

