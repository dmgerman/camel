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
name|io
operator|.
name|InputStream
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
name|CacheManager
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
name|IOHelper
import|;
end_import

begin_class
DECL|class|DefaultCacheManagerFactory
specifier|public
class|class
name|DefaultCacheManagerFactory
extends|extends
name|CacheManagerFactory
block|{
DECL|field|is
specifier|private
name|InputStream
name|is
decl_stmt|;
DECL|method|DefaultCacheManagerFactory ()
specifier|public
name|DefaultCacheManagerFactory
parameter_list|()
block|{
name|this
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
DECL|method|DefaultCacheManagerFactory (InputStream is)
specifier|public
name|DefaultCacheManagerFactory
parameter_list|(
name|InputStream
name|is
parameter_list|)
block|{
name|this
operator|.
name|is
operator|=
name|is
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createCacheManagerInstance ()
specifier|protected
name|CacheManager
name|createCacheManagerInstance
parameter_list|()
block|{
if|if
condition|(
name|is
operator|==
literal|null
condition|)
block|{
comment|// it will still look for "/ehcache.xml" before defaulting to "/ehcache-failsafe.xml"
return|return
name|EHCacheUtil
operator|.
name|createCacheManager
argument_list|()
return|;
block|}
return|return
name|EHCacheUtil
operator|.
name|createCacheManager
argument_list|(
name|is
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|is
operator|!=
literal|null
condition|)
block|{
name|IOHelper
operator|.
name|close
argument_list|(
name|is
argument_list|)
expr_stmt|;
block|}
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

