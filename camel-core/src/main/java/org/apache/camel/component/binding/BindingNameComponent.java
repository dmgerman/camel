begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.binding
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|binding
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
name|DefaultComponent
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
name|spi
operator|.
name|Binding
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
name|CamelContextHelper
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|CamelContextHelper
operator|.
name|getMandatoryEndpoint
import|;
end_import

begin_comment
comment|/**  * A Binding component using the URI form<code>binding:nameOfBinding:endpointURI</code>  * to extract the binding name which is then resolved from the registry and used to create a  * {@link BindingEndpoint} from the underlying {@link Endpoint}  */
end_comment

begin_class
DECL|class|BindingNameComponent
specifier|public
class|class
name|BindingNameComponent
extends|extends
name|DefaultComponent
block|{
DECL|field|BAD_FORMAT_MESSAGE
specifier|protected
specifier|static
specifier|final
name|String
name|BAD_FORMAT_MESSAGE
init|=
literal|"URI should be of the format binding:nameOfBinding:endpointURI"
decl_stmt|;
annotation|@
name|Override
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
name|CamelContext
name|camelContext
init|=
name|getCamelContext
argument_list|()
decl_stmt|;
name|int
name|idx
init|=
name|remaining
operator|.
name|indexOf
argument_list|(
literal|":"
argument_list|)
decl_stmt|;
if|if
condition|(
name|idx
operator|<=
literal|0
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|BAD_FORMAT_MESSAGE
argument_list|)
throw|;
block|}
name|String
name|bindingName
init|=
name|remaining
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|idx
argument_list|)
decl_stmt|;
name|String
name|delegateURI
init|=
name|remaining
operator|.
name|substring
argument_list|(
name|idx
operator|+
literal|1
argument_list|)
decl_stmt|;
if|if
condition|(
name|delegateURI
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|BAD_FORMAT_MESSAGE
argument_list|)
throw|;
block|}
name|Binding
name|binding
init|=
name|CamelContextHelper
operator|.
name|mandatoryLookup
argument_list|(
name|camelContext
argument_list|,
name|bindingName
argument_list|,
name|Binding
operator|.
name|class
argument_list|)
decl_stmt|;
name|Endpoint
name|delegate
init|=
name|getMandatoryEndpoint
argument_list|(
name|camelContext
argument_list|,
name|delegateURI
argument_list|)
decl_stmt|;
return|return
operator|new
name|BindingEndpoint
argument_list|(
name|uri
argument_list|,
name|this
argument_list|,
name|binding
argument_list|,
name|delegate
argument_list|)
return|;
block|}
block|}
end_class

end_unit

