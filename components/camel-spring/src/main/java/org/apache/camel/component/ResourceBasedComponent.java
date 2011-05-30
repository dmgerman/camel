begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
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
name|impl
operator|.
name|DefaultComponent
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
import|import
name|org
operator|.
name|springframework
operator|.
name|core
operator|.
name|io
operator|.
name|Resource
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|core
operator|.
name|io
operator|.
name|ResourceLoader
import|;
end_import

begin_comment
comment|/**  * A useful base class for components which depend on a resource  * such as things like Velocity or XQuery based components.  *  * @version   */
end_comment

begin_class
DECL|class|ResourceBasedComponent
specifier|public
specifier|abstract
class|class
name|ResourceBasedComponent
extends|extends
name|DefaultComponent
block|{
DECL|field|log
specifier|protected
specifier|final
specifier|transient
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
DECL|field|resourceLoader
specifier|private
name|ResourceLoader
name|resourceLoader
decl_stmt|;
DECL|method|getResourceLoader ()
specifier|public
name|ResourceLoader
name|getResourceLoader
parameter_list|()
block|{
if|if
condition|(
name|resourceLoader
operator|==
literal|null
condition|)
block|{
name|resourceLoader
operator|=
operator|new
name|CamelResourceLoader
argument_list|(
name|getCamelContext
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|resourceLoader
return|;
block|}
DECL|method|setResourceLoader (ResourceLoader resourceLoader)
specifier|public
name|void
name|setResourceLoader
parameter_list|(
name|ResourceLoader
name|resourceLoader
parameter_list|)
block|{
name|this
operator|.
name|resourceLoader
operator|=
name|resourceLoader
expr_stmt|;
block|}
DECL|method|resolveMandatoryResource (String uri)
specifier|protected
name|Resource
name|resolveMandatoryResource
parameter_list|(
name|String
name|uri
parameter_list|)
block|{
name|Resource
name|resource
init|=
name|getResourceLoader
argument_list|()
operator|.
name|getResource
argument_list|(
name|uri
argument_list|)
decl_stmt|;
if|if
condition|(
name|resource
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Could not find resource for URI: "
operator|+
name|uri
operator|+
literal|" using: "
operator|+
name|getResourceLoader
argument_list|()
argument_list|)
throw|;
block|}
else|else
block|{
return|return
name|resource
return|;
block|}
block|}
block|}
end_class

end_unit

