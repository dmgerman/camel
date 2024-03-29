begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.xslt
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|xslt
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|ErrorListener
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
name|TransformerException
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
comment|/**  * {@link ErrorListener} which logs the errors and rethrow the exceptions for error and fatal conditions.  */
end_comment

begin_class
DECL|class|XsltErrorListener
specifier|public
class|class
name|XsltErrorListener
implements|implements
name|ErrorListener
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
name|XsltErrorListener
operator|.
name|class
argument_list|)
decl_stmt|;
annotation|@
name|Override
DECL|method|warning (TransformerException e)
specifier|public
name|void
name|warning
parameter_list|(
name|TransformerException
name|e
parameter_list|)
throws|throws
name|TransformerException
block|{
comment|// just log warning
name|LOG
operator|.
name|warn
argument_list|(
literal|"Warning parsing XSLT file: {}"
argument_list|,
name|e
operator|.
name|getMessageAndLocation
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|error (TransformerException e)
specifier|public
name|void
name|error
parameter_list|(
name|TransformerException
name|e
parameter_list|)
throws|throws
name|TransformerException
block|{
name|LOG
operator|.
name|error
argument_list|(
literal|"Error parsing XSLT file: {}"
argument_list|,
name|e
operator|.
name|getMessageAndLocation
argument_list|()
argument_list|)
expr_stmt|;
throw|throw
name|e
throw|;
block|}
annotation|@
name|Override
DECL|method|fatalError (TransformerException e)
specifier|public
name|void
name|fatalError
parameter_list|(
name|TransformerException
name|e
parameter_list|)
throws|throws
name|TransformerException
block|{
name|LOG
operator|.
name|error
argument_list|(
literal|"Fatal error parsing XSLT file: {}"
argument_list|,
name|e
operator|.
name|getMessageAndLocation
argument_list|()
argument_list|)
expr_stmt|;
throw|throw
name|e
throw|;
block|}
block|}
end_class

end_unit

