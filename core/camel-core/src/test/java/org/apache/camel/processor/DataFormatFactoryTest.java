begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
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
name|ContextTestSupport
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
name|JndiRegistry
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
name|dataformat
operator|.
name|SerializationDataFormat
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
name|dataformat
operator|.
name|StringDataFormat
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
name|DataFormat
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
name|DataFormatFactory
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

begin_class
DECL|class|DataFormatFactoryTest
specifier|public
class|class
name|DataFormatFactoryTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|STRING_DF
specifier|private
specifier|static
specifier|final
name|DataFormat
name|STRING_DF
init|=
operator|new
name|StringDataFormat
argument_list|(
literal|"US-ASCII"
argument_list|)
decl_stmt|;
DECL|field|STRING_DFF
specifier|private
specifier|static
specifier|final
name|DataFormatFactory
name|STRING_DFF
init|=
parameter_list|()
lambda|->
operator|new
name|StringDataFormat
argument_list|(
literal|"UTF-8"
argument_list|)
decl_stmt|;
DECL|field|SERIALIZATION_DF
specifier|private
specifier|static
specifier|final
name|DataFormat
name|SERIALIZATION_DF
init|=
operator|new
name|SerializationDataFormat
argument_list|()
decl_stmt|;
annotation|@
name|Test
DECL|method|testDataFormatResolveOrCreate ()
specifier|public
name|void
name|testDataFormatResolveOrCreate
parameter_list|()
throws|throws
name|Exception
block|{
name|assertSame
argument_list|(
name|STRING_DF
argument_list|,
name|context
operator|.
name|resolveDataFormat
argument_list|(
literal|"string"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotSame
argument_list|(
name|STRING_DF
argument_list|,
name|context
operator|.
name|createDataFormat
argument_list|(
literal|"string"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotSame
argument_list|(
name|context
operator|.
name|createDataFormat
argument_list|(
literal|"string"
argument_list|)
argument_list|,
name|context
operator|.
name|createDataFormat
argument_list|(
literal|"string"
argument_list|)
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|SERIALIZATION_DF
argument_list|,
name|context
operator|.
name|resolveDataFormat
argument_list|(
literal|"serialization"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotSame
argument_list|(
name|SERIALIZATION_DF
argument_list|,
name|context
operator|.
name|createDataFormat
argument_list|(
literal|"serialization"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotSame
argument_list|(
name|context
operator|.
name|createDataFormat
argument_list|(
literal|"serialization"
argument_list|)
argument_list|,
name|context
operator|.
name|createDataFormat
argument_list|(
literal|"serialization"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"US-ASCII"
argument_list|,
operator|(
operator|(
name|StringDataFormat
operator|)
name|context
operator|.
name|resolveDataFormat
argument_list|(
literal|"string"
argument_list|)
operator|)
operator|.
name|getCharset
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"UTF-8"
argument_list|,
operator|(
operator|(
name|StringDataFormat
operator|)
name|context
operator|.
name|createDataFormat
argument_list|(
literal|"string"
argument_list|)
operator|)
operator|.
name|getCharset
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createRegistry ()
specifier|protected
name|JndiRegistry
name|createRegistry
parameter_list|()
throws|throws
name|Exception
block|{
name|JndiRegistry
name|registry
init|=
name|super
operator|.
name|createRegistry
argument_list|()
decl_stmt|;
name|registry
operator|.
name|bind
argument_list|(
literal|"string-dataformat"
argument_list|,
name|STRING_DF
argument_list|)
expr_stmt|;
name|registry
operator|.
name|bind
argument_list|(
literal|"string-dataformat-factory"
argument_list|,
name|STRING_DFF
argument_list|)
expr_stmt|;
name|registry
operator|.
name|bind
argument_list|(
literal|"serialization"
argument_list|,
name|SERIALIZATION_DF
argument_list|)
expr_stmt|;
return|return
name|registry
return|;
block|}
block|}
end_class

end_unit

