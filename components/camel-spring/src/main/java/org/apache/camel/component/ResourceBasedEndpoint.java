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
name|Component
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
name|Processor
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
name|impl
operator|.
name|ProcessorEndpoint
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
name|springframework
operator|.
name|core
operator|.
name|io
operator|.
name|DefaultResourceLoader
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
comment|/**  * A useful base class for endpoints which depend on a resource  * such as things like Velocity or XQuery based components  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|ResourceBasedEndpoint
specifier|public
specifier|abstract
class|class
name|ResourceBasedEndpoint
extends|extends
name|ProcessorEndpoint
block|{
DECL|field|log
specifier|protected
specifier|final
specifier|transient
name|Log
name|log
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
DECL|field|resourceUri
specifier|private
specifier|final
name|String
name|resourceUri
decl_stmt|;
DECL|field|resourceLoader
specifier|private
name|ResourceLoader
name|resourceLoader
init|=
operator|new
name|DefaultResourceLoader
argument_list|()
decl_stmt|;
DECL|field|resource
specifier|private
name|Resource
name|resource
decl_stmt|;
DECL|method|ResourceBasedEndpoint (String endpointUri, Component component, String resourceUri, Processor processor)
specifier|public
name|ResourceBasedEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|Component
name|component
parameter_list|,
name|String
name|resourceUri
parameter_list|,
name|Processor
name|processor
parameter_list|)
block|{
name|super
argument_list|(
name|endpointUri
argument_list|,
name|component
argument_list|,
name|processor
argument_list|)
expr_stmt|;
name|this
operator|.
name|resourceUri
operator|=
name|resourceUri
expr_stmt|;
block|}
DECL|method|ResourceBasedEndpoint (String endpointUri, Processor processor, String resourceUri)
specifier|protected
name|ResourceBasedEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|Processor
name|processor
parameter_list|,
name|String
name|resourceUri
parameter_list|)
block|{
name|super
argument_list|(
name|endpointUri
argument_list|,
name|processor
argument_list|)
expr_stmt|;
name|this
operator|.
name|resourceUri
operator|=
name|resourceUri
expr_stmt|;
block|}
DECL|method|getResource ()
specifier|public
name|Resource
name|getResource
parameter_list|()
block|{
if|if
condition|(
name|resource
operator|==
literal|null
condition|)
block|{
name|resource
operator|=
name|getResourceLoader
argument_list|()
operator|.
name|getResource
argument_list|(
name|resourceUri
argument_list|)
expr_stmt|;
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
name|resourceUri
operator|+
literal|" using: "
operator|+
name|getResourceLoader
argument_list|()
argument_list|)
throw|;
block|}
block|}
return|return
name|resource
return|;
block|}
DECL|method|getResourceLoader ()
specifier|public
name|ResourceLoader
name|getResourceLoader
parameter_list|()
block|{
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
block|}
end_class

end_unit

