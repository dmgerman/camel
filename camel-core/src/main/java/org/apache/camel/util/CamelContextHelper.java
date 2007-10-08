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
name|NoSuchEndpointException
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
name|ObjectHelper
operator|.
name|notNull
import|;
end_import

begin_comment
comment|/**  * A number of helper methods  *   * @version $Revision: $  */
end_comment

begin_class
DECL|class|CamelContextHelper
specifier|public
class|class
name|CamelContextHelper
block|{
comment|/**      * Utility classes should not have a public constructor.      */
DECL|method|CamelContextHelper ()
specifier|private
name|CamelContextHelper
parameter_list|()
block|{             }
comment|/**      * Returns the mandatory endpoint for the given URI or the      * {@link org.apache.camel.NoSuchEndpointException} is thrown      *       * @param camelContext      * @param uri      * @return      */
DECL|method|getMandatoryEndpoint (CamelContext camelContext, String uri)
specifier|public
specifier|static
name|Endpoint
name|getMandatoryEndpoint
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|String
name|uri
parameter_list|)
throws|throws
name|NoSuchEndpointException
block|{
name|Endpoint
name|endpoint
init|=
name|camelContext
operator|.
name|getEndpoint
argument_list|(
name|uri
argument_list|)
decl_stmt|;
if|if
condition|(
name|endpoint
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NoSuchEndpointException
argument_list|(
name|uri
argument_list|)
throw|;
block|}
else|else
block|{
return|return
name|endpoint
return|;
block|}
block|}
comment|/**      * Converts the given value to the requested type      */
DECL|method|convertTo (CamelContext context, Class<T> type, Object value)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|T
name|convertTo
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
name|notNull
argument_list|(
name|context
argument_list|,
literal|"camelContext"
argument_list|)
expr_stmt|;
return|return
name|context
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|type
argument_list|,
name|value
argument_list|)
return|;
block|}
comment|/**      * Converts the given value to the specified type throwing an {@link IllegalArgumentException}      * if the value could not be converted to a non null value      */
DECL|method|mandatoryConvertTo (CamelContext context, Class<T> type, Object value)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|T
name|mandatoryConvertTo
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
name|T
name|answer
init|=
name|convertTo
argument_list|(
name|context
argument_list|,
name|type
argument_list|,
name|value
argument_list|)
decl_stmt|;
if|if
condition|(
name|answer
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Value "
operator|+
name|value
operator|+
literal|" converted to "
operator|+
name|type
operator|.
name|getName
argument_list|()
operator|+
literal|" cannot be null"
argument_list|)
throw|;
block|}
return|return
name|answer
return|;
block|}
block|}
end_class

end_unit

