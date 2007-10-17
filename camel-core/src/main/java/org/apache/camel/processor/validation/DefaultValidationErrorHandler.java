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
name|ArrayList
import|;
end_import

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
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|dom
operator|.
name|DOMResult
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|validation
operator|.
name|Schema
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
name|xml
operator|.
name|sax
operator|.
name|SAXException
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

begin_comment
comment|/**  * A default error handler which just stores all the errors so they can be reported or transformed.  *  * @version $Revision: $  */
end_comment

begin_class
DECL|class|DefaultValidationErrorHandler
specifier|public
class|class
name|DefaultValidationErrorHandler
implements|implements
name|ValidatorErrorHandler
block|{
DECL|field|log
specifier|private
specifier|static
specifier|final
specifier|transient
name|Log
name|log
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|DefaultValidationErrorHandler
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|warnings
specifier|private
name|List
argument_list|<
name|SAXParseException
argument_list|>
name|warnings
init|=
operator|new
name|ArrayList
argument_list|<
name|SAXParseException
argument_list|>
argument_list|()
decl_stmt|;
DECL|field|errors
specifier|private
name|List
argument_list|<
name|SAXParseException
argument_list|>
name|errors
init|=
operator|new
name|ArrayList
argument_list|<
name|SAXParseException
argument_list|>
argument_list|()
decl_stmt|;
DECL|field|fatalErrors
specifier|private
name|List
argument_list|<
name|SAXParseException
argument_list|>
name|fatalErrors
init|=
operator|new
name|ArrayList
argument_list|<
name|SAXParseException
argument_list|>
argument_list|()
decl_stmt|;
DECL|method|warning (SAXParseException e)
specifier|public
name|void
name|warning
parameter_list|(
name|SAXParseException
name|e
parameter_list|)
throws|throws
name|SAXException
block|{
if|if
condition|(
name|log
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"warning: "
operator|+
name|e
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
name|warnings
operator|.
name|add
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
DECL|method|error (SAXParseException e)
specifier|public
name|void
name|error
parameter_list|(
name|SAXParseException
name|e
parameter_list|)
throws|throws
name|SAXException
block|{
if|if
condition|(
name|log
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"error: "
operator|+
name|e
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
name|errors
operator|.
name|add
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
DECL|method|fatalError (SAXParseException e)
specifier|public
name|void
name|fatalError
parameter_list|(
name|SAXParseException
name|e
parameter_list|)
throws|throws
name|SAXException
block|{
if|if
condition|(
name|log
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"fatalError: "
operator|+
name|e
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
name|fatalErrors
operator|.
name|add
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
DECL|method|reset ()
specifier|public
name|void
name|reset
parameter_list|()
block|{
name|warnings
operator|.
name|clear
argument_list|()
expr_stmt|;
name|errors
operator|.
name|clear
argument_list|()
expr_stmt|;
name|fatalErrors
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
DECL|method|isValid ()
specifier|public
name|boolean
name|isValid
parameter_list|()
block|{
return|return
name|errors
operator|.
name|isEmpty
argument_list|()
operator|&&
name|fatalErrors
operator|.
name|isEmpty
argument_list|()
return|;
block|}
DECL|method|handleErrors (Exchange exchange, Schema schema, DOMResult result)
specifier|public
name|void
name|handleErrors
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Schema
name|schema
parameter_list|,
name|DOMResult
name|result
parameter_list|)
throws|throws
name|ValidationException
block|{
if|if
condition|(
operator|!
name|isValid
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|SchemaValidationException
argument_list|(
name|exchange
argument_list|,
name|schema
argument_list|,
name|fatalErrors
argument_list|,
name|errors
argument_list|,
name|warnings
argument_list|)
throw|;
block|}
block|}
DECL|method|handleErrors (Exchange exchange, Object schema)
specifier|public
name|void
name|handleErrors
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Object
name|schema
parameter_list|)
throws|throws
name|ValidationException
block|{
if|if
condition|(
operator|!
name|isValid
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|SchemaValidationException
argument_list|(
name|exchange
argument_list|,
name|schema
argument_list|,
name|fatalErrors
argument_list|,
name|errors
argument_list|,
name|warnings
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

