begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cxf.jaxrs
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|cxf
operator|.
name|jaxrs
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayOutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|OutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|Annotation
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|MediaType
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
name|support
operator|.
name|service
operator|.
name|ServiceSupport
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|helpers
operator|.
name|IOUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|jaxrs
operator|.
name|impl
operator|.
name|MetadataMap
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

begin_class
DECL|class|DataFormatProviderTest
specifier|public
class|class
name|DataFormatProviderTest
extends|extends
name|Assert
block|{
annotation|@
name|Test
DECL|method|testIsReadableWriteableSpecificMatch ()
specifier|public
name|void
name|testIsReadableWriteableSpecificMatch
parameter_list|()
block|{
name|DataFormatProvider
argument_list|<
name|Book
argument_list|>
name|p
init|=
operator|new
name|DataFormatProvider
argument_list|<>
argument_list|()
decl_stmt|;
name|p
operator|.
name|setFormat
argument_list|(
literal|"text/plain"
argument_list|,
operator|new
name|TestDataFormat
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|p
operator|.
name|isReadable
argument_list|(
name|Book
operator|.
name|class
argument_list|,
name|Book
operator|.
name|class
argument_list|,
operator|new
name|Annotation
index|[]
block|{}
argument_list|,
name|MediaType
operator|.
name|TEXT_PLAIN_TYPE
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|p
operator|.
name|isWriteable
argument_list|(
name|Book
operator|.
name|class
argument_list|,
name|Book
operator|.
name|class
argument_list|,
operator|new
name|Annotation
index|[]
block|{}
argument_list|,
name|MediaType
operator|.
name|TEXT_PLAIN_TYPE
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testIsReadableWriteableComplexSubMatch ()
specifier|public
name|void
name|testIsReadableWriteableComplexSubMatch
parameter_list|()
block|{
name|DataFormatProvider
argument_list|<
name|Book
argument_list|>
name|p
init|=
operator|new
name|DataFormatProvider
argument_list|<>
argument_list|()
decl_stmt|;
name|p
operator|.
name|setFormat
argument_list|(
literal|"text/plain"
argument_list|,
operator|new
name|TestDataFormat
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|p
operator|.
name|isReadable
argument_list|(
name|Book
operator|.
name|class
argument_list|,
name|Book
operator|.
name|class
argument_list|,
operator|new
name|Annotation
index|[]
block|{}
argument_list|,
name|MediaType
operator|.
name|valueOf
argument_list|(
literal|"text/plain+v2"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|p
operator|.
name|isWriteable
argument_list|(
name|Book
operator|.
name|class
argument_list|,
name|Book
operator|.
name|class
argument_list|,
operator|new
name|Annotation
index|[]
block|{}
argument_list|,
name|MediaType
operator|.
name|valueOf
argument_list|(
literal|"text/plain+v2"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testIsReadableWriteableStarMatch ()
specifier|public
name|void
name|testIsReadableWriteableStarMatch
parameter_list|()
block|{
name|DataFormatProvider
argument_list|<
name|Book
argument_list|>
name|p
init|=
operator|new
name|DataFormatProvider
argument_list|<>
argument_list|()
decl_stmt|;
name|p
operator|.
name|setFormat
argument_list|(
operator|new
name|TestDataFormat
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|p
operator|.
name|isReadable
argument_list|(
name|Book
operator|.
name|class
argument_list|,
name|Book
operator|.
name|class
argument_list|,
operator|new
name|Annotation
index|[]
block|{}
argument_list|,
name|MediaType
operator|.
name|TEXT_PLAIN_TYPE
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|p
operator|.
name|isWriteable
argument_list|(
name|Book
operator|.
name|class
argument_list|,
name|Book
operator|.
name|class
argument_list|,
operator|new
name|Annotation
index|[]
block|{}
argument_list|,
name|MediaType
operator|.
name|TEXT_PLAIN_TYPE
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testNotReadableWriteable ()
specifier|public
name|void
name|testNotReadableWriteable
parameter_list|()
block|{
name|DataFormatProvider
argument_list|<
name|Book
argument_list|>
name|p
init|=
operator|new
name|DataFormatProvider
argument_list|<>
argument_list|()
decl_stmt|;
name|p
operator|.
name|setFormat
argument_list|(
literal|"application/json"
argument_list|,
operator|new
name|TestDataFormat
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|p
operator|.
name|isReadable
argument_list|(
name|Book
operator|.
name|class
argument_list|,
name|Book
operator|.
name|class
argument_list|,
operator|new
name|Annotation
index|[]
block|{}
argument_list|,
name|MediaType
operator|.
name|TEXT_PLAIN_TYPE
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|p
operator|.
name|isWriteable
argument_list|(
name|Book
operator|.
name|class
argument_list|,
name|Book
operator|.
name|class
argument_list|,
operator|new
name|Annotation
index|[]
block|{}
argument_list|,
name|MediaType
operator|.
name|TEXT_PLAIN_TYPE
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testReadFrom ()
specifier|public
name|void
name|testReadFrom
parameter_list|()
throws|throws
name|Exception
block|{
name|DataFormatProvider
argument_list|<
name|Book
argument_list|>
name|p
init|=
operator|new
name|DataFormatProvider
argument_list|<>
argument_list|()
decl_stmt|;
name|p
operator|.
name|setFormat
argument_list|(
literal|"text/plain"
argument_list|,
operator|new
name|TestDataFormat
argument_list|()
argument_list|)
expr_stmt|;
name|ByteArrayInputStream
name|bis
init|=
operator|new
name|ByteArrayInputStream
argument_list|(
literal|"dataformat"
operator|.
name|getBytes
argument_list|()
argument_list|)
decl_stmt|;
name|Book
name|b
init|=
name|p
operator|.
name|readFrom
argument_list|(
name|Book
operator|.
name|class
argument_list|,
name|Book
operator|.
name|class
argument_list|,
operator|new
name|Annotation
index|[]
block|{}
argument_list|,
name|MediaType
operator|.
name|TEXT_PLAIN_TYPE
argument_list|,
operator|new
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
argument_list|,
name|bis
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"dataformat"
argument_list|,
name|b
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testWriteTo ()
specifier|public
name|void
name|testWriteTo
parameter_list|()
throws|throws
name|Exception
block|{
name|DataFormatProvider
argument_list|<
name|Book
argument_list|>
name|p
init|=
operator|new
name|DataFormatProvider
argument_list|<>
argument_list|()
decl_stmt|;
name|p
operator|.
name|setFormat
argument_list|(
literal|"text/plain"
argument_list|,
operator|new
name|TestDataFormat
argument_list|()
argument_list|)
expr_stmt|;
name|ByteArrayOutputStream
name|bos
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|p
operator|.
name|writeTo
argument_list|(
operator|new
name|Book
argument_list|(
literal|"dataformat"
argument_list|)
argument_list|,
name|Book
operator|.
name|class
argument_list|,
name|Book
operator|.
name|class
argument_list|,
operator|new
name|Annotation
index|[]
block|{}
argument_list|,
name|MediaType
operator|.
name|TEXT_PLAIN_TYPE
argument_list|,
operator|new
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
argument_list|,
name|bos
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"dataformat"
argument_list|,
name|bos
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|class|Book
specifier|private
specifier|static
class|class
name|Book
block|{
DECL|field|name
specifier|private
name|String
name|name
decl_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unused"
argument_list|)
DECL|method|Book ()
name|Book
parameter_list|()
block|{         }
DECL|method|Book (String name)
name|Book
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
block|}
DECL|method|getName ()
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|name
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unused"
argument_list|)
DECL|method|setName (String name)
specifier|public
name|void
name|setName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
block|}
block|}
DECL|class|TestDataFormat
specifier|private
specifier|static
class|class
name|TestDataFormat
extends|extends
name|ServiceSupport
implements|implements
name|DataFormat
block|{
annotation|@
name|Override
DECL|method|marshal (Exchange ex, Object obj, OutputStream os)
specifier|public
name|void
name|marshal
parameter_list|(
name|Exchange
name|ex
parameter_list|,
name|Object
name|obj
parameter_list|,
name|OutputStream
name|os
parameter_list|)
throws|throws
name|Exception
block|{
name|os
operator|.
name|write
argument_list|(
operator|(
operator|(
name|Book
operator|)
name|obj
operator|)
operator|.
name|getName
argument_list|()
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
name|os
operator|.
name|flush
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|unmarshal (Exchange ex, InputStream is)
specifier|public
name|Object
name|unmarshal
parameter_list|(
name|Exchange
name|ex
parameter_list|,
name|InputStream
name|is
parameter_list|)
throws|throws
name|Exception
block|{
return|return
operator|new
name|Book
argument_list|(
name|IOUtils
operator|.
name|readStringFromStream
argument_list|(
name|is
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
comment|// noop
block|}
annotation|@
name|Override
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
comment|// noop
block|}
block|}
block|}
end_class

end_unit

