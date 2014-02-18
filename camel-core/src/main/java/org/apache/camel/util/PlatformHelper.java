begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.util
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
package|;
end_package

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Method
import|;
end_import

begin_import
import|import static
name|java
operator|.
name|lang
operator|.
name|Thread
operator|.
name|currentThread
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

begin_comment
comment|/**  * Utility dedicated for resolving runtime information related to the platform on which Camel is currently running.  */
end_comment

begin_class
DECL|class|PlatformHelper
specifier|public
specifier|final
class|class
name|PlatformHelper
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
name|PlatformHelper
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|method|PlatformHelper ()
specifier|private
name|PlatformHelper
parameter_list|()
block|{     }
comment|/**      * Determine whether Camel is running in the OSGi environment. Current implementation tries to load Camel activator      * bundle (using reflection API and class loading) to determine if the code is executed in the OSGi environment.      *      * @return true if caller is running in the OSGi environment, false otherwise      */
DECL|method|isInOsgiEnvironment ()
specifier|public
specifier|static
name|boolean
name|isInOsgiEnvironment
parameter_list|()
block|{
try|try
block|{
comment|// Try to load the BundleActivator first
name|Class
operator|.
name|forName
argument_list|(
literal|"org.osgi.framework.BundleActivator"
argument_list|)
expr_stmt|;
name|Class
argument_list|<
name|?
argument_list|>
name|activatorClass
init|=
name|currentThread
argument_list|()
operator|.
name|getContextClassLoader
argument_list|()
operator|.
name|loadClass
argument_list|(
literal|"org.apache.camel.osgi.Activator"
argument_list|)
decl_stmt|;
name|Method
name|getBundleMethod
init|=
name|activatorClass
operator|.
name|getDeclaredMethod
argument_list|(
literal|"getBundle"
argument_list|)
decl_stmt|;
name|Object
name|bundle
init|=
name|getBundleMethod
operator|.
name|invoke
argument_list|(
literal|null
argument_list|)
decl_stmt|;
return|return
name|bundle
operator|!=
literal|null
return|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Cannot find class so assuming not running in OSGi container: "
operator|+
name|t
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
block|}
block|}
end_class

end_unit

