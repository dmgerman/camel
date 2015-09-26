begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jolt
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jolt
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|com
operator|.
name|bazaarvoice
operator|.
name|jolt
operator|.
name|Transform
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
name|Endpoint
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
name|UriEndpointComponent
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
name|ResourceHelper
import|;
end_import

begin_class
DECL|class|JoltComponent
specifier|public
class|class
name|JoltComponent
extends|extends
name|UriEndpointComponent
block|{
DECL|field|transform
specifier|private
name|Transform
name|transform
decl_stmt|;
DECL|method|JoltComponent ()
specifier|public
name|JoltComponent
parameter_list|()
block|{
name|super
argument_list|(
name|JoltEndpoint
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
DECL|method|createEndpoint (String uri, String remaining, Map<String, Object> parameters)
specifier|protected
name|Endpoint
name|createEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|remaining
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|)
throws|throws
name|Exception
block|{
name|boolean
name|cache
init|=
name|getAndRemoveParameter
argument_list|(
name|parameters
argument_list|,
literal|"contentCache"
argument_list|,
name|Boolean
operator|.
name|class
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
decl_stmt|;
name|JoltEndpoint
name|answer
init|=
operator|new
name|JoltEndpoint
argument_list|(
name|uri
argument_list|,
name|this
argument_list|,
name|remaining
argument_list|)
decl_stmt|;
name|answer
operator|.
name|setContentCache
argument_list|(
name|cache
argument_list|)
expr_stmt|;
name|answer
operator|.
name|setTransform
argument_list|(
name|transform
argument_list|)
expr_stmt|;
comment|// if its a http resource then append any remaining parameters and update the resource uri
if|if
condition|(
name|ResourceHelper
operator|.
name|isHttpUri
argument_list|(
name|remaining
argument_list|)
condition|)
block|{
name|remaining
operator|=
name|ResourceHelper
operator|.
name|appendParameters
argument_list|(
name|remaining
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
name|answer
operator|.
name|setResourceUri
argument_list|(
name|remaining
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
DECL|method|getTransform ()
specifier|public
name|Transform
name|getTransform
parameter_list|()
block|{
return|return
name|transform
return|;
block|}
comment|/**      * Explicitly sets the Transform to use. If not set a Transform specified by the transformDsl will be created      */
DECL|method|setTransform (Transform transform)
specifier|public
name|void
name|setTransform
parameter_list|(
name|Transform
name|transform
parameter_list|)
block|{
name|this
operator|.
name|transform
operator|=
name|transform
expr_stmt|;
block|}
block|}
end_class

end_unit

