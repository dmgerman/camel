begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor.validation
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
operator|.
name|validation
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|org
operator|.
name|xml
operator|.
name|sax
operator|.
name|SAXParseException
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
name|Exchange
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
name|ValidationException
import|;
end_import

begin_comment
comment|/**  * A Schema validation exception occurred  *   * @version $Revision$  */
end_comment

begin_class
DECL|class|SchemaValidationException
specifier|public
class|class
name|SchemaValidationException
extends|extends
name|ValidationException
block|{
DECL|field|schema
specifier|private
specifier|final
name|Object
name|schema
decl_stmt|;
DECL|field|fatalErrors
specifier|private
specifier|final
name|List
argument_list|<
name|SAXParseException
argument_list|>
name|fatalErrors
decl_stmt|;
DECL|field|errors
specifier|private
specifier|final
name|List
argument_list|<
name|SAXParseException
argument_list|>
name|errors
decl_stmt|;
DECL|field|warnings
specifier|private
specifier|final
name|List
argument_list|<
name|SAXParseException
argument_list|>
name|warnings
decl_stmt|;
DECL|method|SchemaValidationException (Exchange exchange, Object schema, List<SAXParseException> fatalErrors, List<SAXParseException> errors, List<SAXParseException> warnings)
specifier|public
name|SchemaValidationException
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Object
name|schema
parameter_list|,
name|List
argument_list|<
name|SAXParseException
argument_list|>
name|fatalErrors
parameter_list|,
name|List
argument_list|<
name|SAXParseException
argument_list|>
name|errors
parameter_list|,
name|List
argument_list|<
name|SAXParseException
argument_list|>
name|warnings
parameter_list|)
block|{
name|super
argument_list|(
name|exchange
argument_list|,
name|message
argument_list|(
name|schema
argument_list|,
name|fatalErrors
argument_list|,
name|errors
argument_list|,
name|warnings
argument_list|)
argument_list|)
expr_stmt|;
name|this
operator|.
name|schema
operator|=
name|schema
expr_stmt|;
name|this
operator|.
name|fatalErrors
operator|=
name|fatalErrors
expr_stmt|;
name|this
operator|.
name|errors
operator|=
name|errors
expr_stmt|;
name|this
operator|.
name|warnings
operator|=
name|warnings
expr_stmt|;
block|}
comment|/**      * Returns the schema that failed      */
DECL|method|getSchema ()
specifier|public
name|Object
name|getSchema
parameter_list|()
block|{
return|return
name|schema
return|;
block|}
comment|/**      * Returns the validation errors      */
DECL|method|getErrors ()
specifier|public
name|List
argument_list|<
name|SAXParseException
argument_list|>
name|getErrors
parameter_list|()
block|{
return|return
name|errors
return|;
block|}
comment|/**      * Returns the fatal validation errors      */
DECL|method|getFatalErrors ()
specifier|public
name|List
argument_list|<
name|SAXParseException
argument_list|>
name|getFatalErrors
parameter_list|()
block|{
return|return
name|fatalErrors
return|;
block|}
comment|/**      * Returns the validation warnings      */
DECL|method|getWarnings ()
specifier|public
name|List
argument_list|<
name|SAXParseException
argument_list|>
name|getWarnings
parameter_list|()
block|{
return|return
name|warnings
return|;
block|}
DECL|method|message (Object schema, List<SAXParseException> fatalErrors, List<SAXParseException> errors, List<SAXParseException> warnings)
specifier|protected
specifier|static
name|String
name|message
parameter_list|(
name|Object
name|schema
parameter_list|,
name|List
argument_list|<
name|SAXParseException
argument_list|>
name|fatalErrors
parameter_list|,
name|List
argument_list|<
name|SAXParseException
argument_list|>
name|errors
parameter_list|,
name|List
argument_list|<
name|SAXParseException
argument_list|>
name|warnings
parameter_list|)
block|{
name|StringBuffer
name|buffer
init|=
operator|new
name|StringBuffer
argument_list|(
literal|"Validation failed for: "
operator|+
name|schema
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|fatalErrors
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|buffer
operator|.
name|append
argument_list|(
literal|" fatal errors: "
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
name|fatalErrors
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|errors
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|buffer
operator|.
name|append
argument_list|(
literal|" errors: "
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
name|errors
argument_list|)
expr_stmt|;
block|}
return|return
name|buffer
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

