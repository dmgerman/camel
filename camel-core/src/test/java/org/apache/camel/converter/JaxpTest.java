begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.converter
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|converter
package|;
end_package

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
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|Source
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
name|DOMSource
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
name|stream
operator|.
name|StreamSource
import|;
end_import

begin_import
import|import
name|junit
operator|.
name|framework
operator|.
name|TestCase
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
name|TypeConverter
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
name|ReflectionInjector
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
name|converter
operator|.
name|DefaultTypeConverter
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
name|w3c
operator|.
name|dom
operator|.
name|Document
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Element
import|;
end_import

begin_comment
comment|/**  * @version $Revision$  */
end_comment

begin_class
DECL|class|JaxpTest
specifier|public
class|class
name|JaxpTest
extends|extends
name|TestCase
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
specifier|transient
name|Log
name|LOG
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|JaxpTest
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|converter
specifier|protected
name|TypeConverter
name|converter
init|=
operator|new
name|DefaultTypeConverter
argument_list|(
operator|new
name|ReflectionInjector
argument_list|()
argument_list|)
decl_stmt|;
DECL|method|testConvertToDocument ()
specifier|public
name|void
name|testConvertToDocument
parameter_list|()
throws|throws
name|Exception
block|{
name|Document
name|document
init|=
name|converter
operator|.
name|convertTo
argument_list|(
name|Document
operator|.
name|class
argument_list|,
literal|"<?xml version=\"1.0\" encoding=\"UTF-8\"?><hello>world!</hello>"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|document
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Found document: "
operator|+
name|document
argument_list|)
expr_stmt|;
comment|// lets now convert back again
name|String
name|text
init|=
name|converter
operator|.
name|convertTo
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|document
argument_list|)
decl_stmt|;
comment|// The preamble changes a little under Java 1.6 it adds a
comment|// standalone="no" attribute.
name|assertTrue
argument_list|(
literal|"Converted to String: "
operator|+
name|text
argument_list|,
name|text
operator|.
name|endsWith
argument_list|(
literal|"<hello>world!</hello>"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testConvertToSource ()
specifier|public
name|void
name|testConvertToSource
parameter_list|()
throws|throws
name|Exception
block|{
name|Source
name|source
init|=
name|converter
operator|.
name|convertTo
argument_list|(
name|Source
operator|.
name|class
argument_list|,
literal|"<hello>world!</hello>"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|source
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Found document: "
operator|+
name|source
argument_list|)
expr_stmt|;
block|}
DECL|method|testStreamSourceToDomSource ()
specifier|public
name|void
name|testStreamSourceToDomSource
parameter_list|()
throws|throws
name|Exception
block|{
name|StreamSource
name|streamSource
init|=
operator|new
name|StreamSource
argument_list|(
operator|new
name|StringReader
argument_list|(
literal|"<hello>world!</hello>"
argument_list|)
argument_list|)
decl_stmt|;
name|DOMSource
name|domSource
init|=
name|converter
operator|.
name|convertTo
argument_list|(
name|DOMSource
operator|.
name|class
argument_list|,
name|streamSource
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Could not convert to a DOMSource!"
argument_list|,
name|domSource
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Found document: "
operator|+
name|domSource
argument_list|)
expr_stmt|;
block|}
DECL|method|testNodeToSource ()
specifier|public
name|void
name|testNodeToSource
parameter_list|()
throws|throws
name|Exception
block|{
name|Document
name|document
init|=
name|converter
operator|.
name|convertTo
argument_list|(
name|Document
operator|.
name|class
argument_list|,
literal|"<?xml version=\"1.0\"?><hello>world!</hello>"
argument_list|)
decl_stmt|;
name|Element
name|element
init|=
name|document
operator|.
name|getDocumentElement
argument_list|()
decl_stmt|;
name|Source
name|source
init|=
name|converter
operator|.
name|convertTo
argument_list|(
name|Source
operator|.
name|class
argument_list|,
name|element
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Could not convert from Node to Source!"
argument_list|,
name|source
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Found source: "
operator|+
name|source
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

