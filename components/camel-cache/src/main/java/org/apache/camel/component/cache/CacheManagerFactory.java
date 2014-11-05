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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|support
operator|.
name|ServiceSupport
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
name|ReflectionHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_class
DECL|class|CacheManagerFactory
specifier|public
specifier|abstract
class|class
name|CacheManagerFactory
extends|extends
name|ServiceSupport
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|CacheManagerFactory
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|cacheManager
specifier|private
name|CacheManager
name|cacheManager
decl_stmt|;
DECL|method|getInstance ()
specifier|public
specifier|synchronized
name|CacheManager
name|getInstance
parameter_list|()
block|{
if|if
condition|(
name|cacheManager
operator|==
literal|null
condition|)
block|{
name|cacheManager
operator|=
name|createCacheManagerInstance
argument_list|()
expr_stmt|;
comment|// always turn off ET phone-home
name|LOG
operator|.
name|debug
argument_list|(
literal|"Turning off EHCache update checker ..."
argument_list|)
expr_stmt|;
name|Configuration
name|config
init|=
name|cacheManager
operator|.
name|getConfiguration
argument_list|()
decl_stmt|;
try|try
block|{
comment|// need to set both the system property and bypass the setUpdateCheck method as that can be changed dynamically
name|System
operator|.
name|setProperty
argument_list|(
literal|"net.sf.ehcache.skipUpdateCheck"
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
name|ReflectionHelper
operator|.
name|setField
argument_list|(
name|config
operator|.
name|getClass
argument_list|()
operator|.
name|getDeclaredField
argument_list|(
literal|"updateCheck"
argument_list|)
argument_list|,
name|config
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Turned off EHCache update checker. updateCheck={}"
argument_list|,
name|config
operator|.
name|getUpdateCheck
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
comment|// ignore
name|LOG
operator|.
name|warn
argument_list|(
literal|"Error turning off EHCache update checker. Beware information sent over the internet!"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|cacheManager
return|;
block|}
comment|/**      * Creates {@link CacheManager}.      *<p/>      * The default implementation is {@link DefaultCacheManagerFactory}.      *      * @return {@link CacheManager}      */
DECL|method|createCacheManagerInstance ()
specifier|protected
specifier|abstract
name|CacheManager
name|createCacheManagerInstance
parameter_list|()
function_decl|;
annotation|@
name|Override
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{     }
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
comment|// shutdown cache manager when stopping
if|if
condition|(
name|cacheManager
operator|!=
literal|null
condition|)
block|{
name|cacheManager
operator|.
name|shutdown
argument_list|()
expr_stmt|;
name|cacheManager
operator|=
literal|null
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

