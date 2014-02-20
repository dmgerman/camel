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
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|Bundle
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

begin_import
import|import static
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|FrameworkUtil
operator|.
name|getBundle
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
comment|/**      * Determine whether Camel is running in the OSGi environment.      *      * @param classFromBundle class to be tested against being deployed into OSGi      * @return true if caller is running in the OSGi environment, false otherwise      */
DECL|method|isInOsgiEnvironment (Class classFromBundle)
specifier|public
specifier|static
name|boolean
name|isInOsgiEnvironment
parameter_list|(
name|Class
name|classFromBundle
parameter_list|)
block|{
name|Bundle
name|bundle
init|=
name|getBundle
argument_list|(
name|classFromBundle
argument_list|)
decl_stmt|;
if|if
condition|(
name|bundle
operator|!=
literal|null
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Found OSGi bundle {} for class {} so assuming running in the OSGi container."
argument_list|,
name|bundle
operator|.
name|getSymbolicName
argument_list|()
argument_list|,
name|classFromBundle
operator|.
name|getSimpleName
argument_list|()
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
else|else
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Cannot find OSGi bundle for class {} so assuming not running in the OSGi container."
argument_list|,
name|classFromBundle
operator|.
name|getSimpleName
argument_list|()
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
block|}
DECL|method|isInOsgiEnvironment ()
specifier|public
specifier|static
name|boolean
name|isInOsgiEnvironment
parameter_list|()
block|{
return|return
name|isInOsgiEnvironment
argument_list|(
name|PlatformHelper
operator|.
name|class
argument_list|)
return|;
block|}
block|}
end_class

end_unit

