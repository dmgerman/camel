begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.support
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|support
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
name|CamelContext
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
comment|/**      * Determine whether Camel is OSGi-aware. Current implementation of the method checks if the name of the      * {@link CamelContext} matches the names of the known OSGi-aware contexts.      *      * @param camelContext context to be tested against OSGi-awareness      * @return true if given context is OSGi-aware, false otherwise      */
DECL|method|isOsgiContext (CamelContext camelContext)
specifier|public
specifier|static
name|boolean
name|isOsgiContext
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|)
block|{
name|String
name|contextType
init|=
name|camelContext
operator|.
name|getClass
argument_list|()
operator|.
name|getSimpleName
argument_list|()
decl_stmt|;
if|if
condition|(
name|contextType
operator|.
name|startsWith
argument_list|(
literal|"Osgi"
argument_list|)
operator|||
name|contextType
operator|.
name|equals
argument_list|(
literal|"BlueprintCamelContext"
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"{} used - assuming running in the OSGi container."
argument_list|,
name|contextType
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
literal|"{} used - assuming running in the OSGi container."
argument_list|,
name|contextType
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

