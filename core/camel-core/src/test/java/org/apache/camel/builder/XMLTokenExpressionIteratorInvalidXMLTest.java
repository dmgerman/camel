begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.builder
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|builder
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Closeable
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|StringReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|text
operator|.
name|MessageFormat
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
import|;
end_import

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
name|javax
operator|.
name|xml
operator|.
name|stream
operator|.
name|XMLStreamException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Assert
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_comment
comment|/**  *  */
end_comment

begin_class
DECL|class|XMLTokenExpressionIteratorInvalidXMLTest
specifier|public
class|class
name|XMLTokenExpressionIteratorInvalidXMLTest
extends|extends
name|Assert
block|{
DECL|field|DATA_TEMPLATE
specifier|private
specifier|static
specifier|final
name|String
name|DATA_TEMPLATE
init|=
literal|"<?xml version=\"1.0\" encoding=\"utf-u\"?>"
operator|+
literal|"<Statements xmlns=\"http://www.apache.org/xml/test\">"
operator|+
literal|"<statement>Hello World</statement>"
operator|+
literal|"<statement>{0}</statement>"
operator|+
literal|"</Statements>"
decl_stmt|;
DECL|field|NSMAP
specifier|private
specifier|static
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|NSMAP
init|=
name|Collections
operator|.
name|singletonMap
argument_list|(
literal|""
argument_list|,
literal|"http://www.apache.org/xml/test"
argument_list|)
decl_stmt|;
annotation|@
name|Test
DECL|method|testExtractToken ()
specifier|public
name|void
name|testExtractToken
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|data
init|=
name|MessageFormat
operator|.
name|format
argument_list|(
name|DATA_TEMPLATE
argument_list|,
literal|"Have a nice day"
argument_list|)
decl_stmt|;
name|XMLTokenExpressionIterator
name|xtei
init|=
operator|new
name|XMLTokenExpressionIterator
argument_list|(
literal|"//statement"
argument_list|,
literal|'i'
argument_list|)
decl_stmt|;
name|xtei
operator|.
name|setNamespaces
argument_list|(
name|NSMAP
argument_list|)
expr_stmt|;
name|invokeAndVerify
argument_list|(
name|xtei
operator|.
name|createIterator
argument_list|(
operator|new
name|StringReader
argument_list|(
name|data
argument_list|)
argument_list|)
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|data
operator|=
name|MessageFormat
operator|.
name|format
argument_list|(
name|DATA_TEMPLATE
argument_list|,
literal|"Have a nice< day"
argument_list|)
expr_stmt|;
name|xtei
operator|=
operator|new
name|XMLTokenExpressionIterator
argument_list|(
literal|"//statement"
argument_list|,
literal|'i'
argument_list|)
expr_stmt|;
name|xtei
operator|.
name|setNamespaces
argument_list|(
name|NSMAP
argument_list|)
expr_stmt|;
name|invokeAndVerify
argument_list|(
name|xtei
operator|.
name|createIterator
argument_list|(
operator|new
name|StringReader
argument_list|(
name|data
argument_list|)
argument_list|)
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
DECL|method|invokeAndVerify (Iterator<?> tokenizer, boolean error)
specifier|private
name|void
name|invokeAndVerify
parameter_list|(
name|Iterator
argument_list|<
name|?
argument_list|>
name|tokenizer
parameter_list|,
name|boolean
name|error
parameter_list|)
throws|throws
name|IOException
throws|,
name|XMLStreamException
block|{
name|Exception
name|exp
init|=
literal|null
decl_stmt|;
try|try
block|{
name|tokenizer
operator|.
name|next
argument_list|()
expr_stmt|;
name|tokenizer
operator|.
name|next
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|exp
operator|=
name|e
expr_stmt|;
block|}
finally|finally
block|{
operator|(
operator|(
name|Closeable
operator|)
name|tokenizer
operator|)
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|error
condition|)
block|{
name|assertNotNull
argument_list|(
literal|"the error expected"
argument_list|,
name|exp
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|assertNull
argument_list|(
literal|"no error expected"
argument_list|,
name|exp
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

