begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
package|;
end_package

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
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|zip
operator|.
name|Deflater
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|zip
operator|.
name|Inflater
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
name|NoTypeConversionAvailableException
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
name|Processor
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
name|builder
operator|.
name|RouteBuilder
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
name|component
operator|.
name|mock
operator|.
name|MockEndpoint
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
name|converter
operator|.
name|stream
operator|.
name|InputStreamCache
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
name|ZipDataFormat
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
name|DefaultExchange
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
comment|/**  * Unit test of the zip data format.  */
end_comment

begin_class
DECL|class|ZipDataFormatTest
specifier|public
class|class
name|ZipDataFormatTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|TEXT
specifier|private
specifier|static
specifier|final
name|String
name|TEXT
init|=
literal|"The Cow in Apple Time \n"
operator|+
literal|"by: Robert Frost \n\n"
operator|+
literal|"Something inspires the only cow of late\n"
operator|+
literal|"To make no more of a wall than an open gate,\n"
operator|+
literal|"And think no more of wall-builders than fools.\n"
operator|+
literal|"Her face is flecked with pomace and she drools\n"
operator|+
literal|"A cider syrup. Having tasted fruit,\n"
operator|+
literal|"She scorns a pasture withering to the root.\n"
operator|+
literal|"She runs from tree to tree where lie and sweeten.\n"
operator|+
literal|"The windfalls spiked with stubble and worm-eaten.\n"
operator|+
literal|"She leaves them bitten when she has to fly.\n"
operator|+
literal|"She bellows on a knoll against the sky.\n"
operator|+
literal|"Her udder shrivels and the milk goes dry."
decl_stmt|;
annotation|@
name|Override
DECL|method|isUseRouteBuilder ()
specifier|public
name|boolean
name|isUseRouteBuilder
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
annotation|@
name|Test
DECL|method|testMarshalMandatoryConversionFailed ()
specifier|public
name|void
name|testMarshalMandatoryConversionFailed
parameter_list|()
throws|throws
name|Exception
block|{
name|DataFormat
name|dataFormat
init|=
operator|new
name|ZipDataFormat
argument_list|()
decl_stmt|;
try|try
block|{
name|dataFormat
operator|.
name|marshal
argument_list|(
operator|new
name|DefaultExchange
argument_list|(
operator|new
name|DefaultCamelContext
argument_list|()
argument_list|)
argument_list|,
operator|new
name|Object
argument_list|()
argument_list|,
operator|new
name|ByteArrayOutputStream
argument_list|()
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should have thrown an exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NoTypeConversionAvailableException
name|e
parameter_list|)
block|{
comment|// expected
block|}
block|}
annotation|@
name|Test
DECL|method|testMarshalTextToZipBestCompression ()
specifier|public
name|void
name|testMarshalTextToZipBestCompression
parameter_list|()
throws|throws
name|Exception
block|{
name|context
operator|.
name|addRoutes
argument_list|(
operator|new
name|RouteBuilder
argument_list|()
block|{
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|marshal
argument_list|()
operator|.
name|zip
argument_list|(
name|Deflater
operator|.
name|BEST_COMPRESSION
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|ZippedMessageProcessor
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|sendText
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testMarshalTextToZipBestSpeed ()
specifier|public
name|void
name|testMarshalTextToZipBestSpeed
parameter_list|()
throws|throws
name|Exception
block|{
name|context
operator|.
name|addRoutes
argument_list|(
operator|new
name|RouteBuilder
argument_list|()
block|{
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|marshal
argument_list|()
operator|.
name|zip
argument_list|(
name|Deflater
operator|.
name|BEST_SPEED
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|ZippedMessageProcessor
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|sendText
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testMarshalTextToZipDefaultCompression ()
specifier|public
name|void
name|testMarshalTextToZipDefaultCompression
parameter_list|()
throws|throws
name|Exception
block|{
name|context
operator|.
name|addRoutes
argument_list|(
operator|new
name|RouteBuilder
argument_list|()
block|{
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|marshal
argument_list|()
operator|.
name|zip
argument_list|(
name|Deflater
operator|.
name|DEFAULT_COMPRESSION
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|ZippedMessageProcessor
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|sendText
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testUnMarshalTextToZip ()
specifier|public
name|void
name|testUnMarshalTextToZip
parameter_list|()
throws|throws
name|Exception
block|{
name|context
operator|.
name|addRoutes
argument_list|(
operator|new
name|RouteBuilder
argument_list|()
block|{
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|marshal
argument_list|()
operator|.
name|zip
argument_list|()
operator|.
name|unmarshal
argument_list|()
operator|.
name|zip
argument_list|()
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|MockEndpoint
name|result
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"mock:result"
argument_list|,
name|MockEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|result
operator|.
name|expectedBodiesReceived
argument_list|(
name|TEXT
argument_list|)
expr_stmt|;
name|sendText
argument_list|()
expr_stmt|;
name|result
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|Exchange
argument_list|>
name|exchangeList
init|=
name|result
operator|.
name|getExchanges
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|exchangeList
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
operator|instanceof
name|byte
index|[]
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testStreamCacheUnzip ()
specifier|public
name|void
name|testStreamCacheUnzip
parameter_list|()
throws|throws
name|Exception
block|{
name|context
operator|.
name|addRoutes
argument_list|(
operator|new
name|RouteBuilder
argument_list|()
block|{
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|streamCaching
argument_list|()
operator|.
name|marshal
argument_list|()
operator|.
name|zip
argument_list|()
operator|.
name|unmarshal
argument_list|()
operator|.
name|zip
argument_list|()
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|MockEndpoint
name|result
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"mock:result"
argument_list|,
name|MockEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|result
operator|.
name|expectedBodiesReceived
argument_list|(
name|TEXT
argument_list|)
expr_stmt|;
name|sendText
argument_list|()
expr_stmt|;
name|result
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|Exchange
argument_list|>
name|exchangeList
init|=
name|result
operator|.
name|getExchanges
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|exchangeList
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
operator|instanceof
name|InputStreamCache
argument_list|)
expr_stmt|;
block|}
DECL|method|sendText ()
specifier|private
name|void
name|sendText
parameter_list|()
throws|throws
name|Exception
block|{
name|template
operator|.
name|sendBodyAndProperty
argument_list|(
literal|"direct:start"
argument_list|,
name|TEXT
argument_list|,
name|Exchange
operator|.
name|CHARSET_NAME
argument_list|,
literal|"UTF-8"
argument_list|)
expr_stmt|;
block|}
DECL|class|ZippedMessageProcessor
specifier|private
specifier|static
class|class
name|ZippedMessageProcessor
implements|implements
name|Processor
block|{
DECL|method|process (Exchange exchange)
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|byte
index|[]
name|body
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|byte
index|[]
operator|.
expr|class
argument_list|)
decl_stmt|;
name|Inflater
name|inflater
init|=
operator|new
name|Inflater
argument_list|()
decl_stmt|;
name|inflater
operator|.
name|setInput
argument_list|(
name|body
argument_list|)
expr_stmt|;
comment|// Create an expandable byte array to hold the inflated data
name|ByteArrayOutputStream
name|bos
init|=
operator|new
name|ByteArrayOutputStream
argument_list|(
name|body
operator|.
name|length
argument_list|)
decl_stmt|;
comment|// Inflate the compressed data
name|byte
index|[]
name|buf
init|=
operator|new
name|byte
index|[
literal|1024
index|]
decl_stmt|;
while|while
condition|(
operator|!
name|inflater
operator|.
name|finished
argument_list|()
condition|)
block|{
name|int
name|count
init|=
name|inflater
operator|.
name|inflate
argument_list|(
name|buf
argument_list|)
decl_stmt|;
name|bos
operator|.
name|write
argument_list|(
name|buf
argument_list|,
literal|0
argument_list|,
name|count
argument_list|)
expr_stmt|;
block|}
name|String
name|result
init|=
operator|new
name|String
argument_list|(
name|bos
operator|.
name|toByteArray
argument_list|()
argument_list|,
literal|"UTF-8"
argument_list|)
decl_stmt|;
comment|// does the testing
name|assertEquals
argument_list|(
name|TEXT
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

