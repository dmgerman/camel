begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.printer
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|printer
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileInputStream
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
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|print
operator|.
name|Doc
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|print
operator|.
name|DocFlavor
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|print
operator|.
name|DocPrintJob
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|print
operator|.
name|PrintService
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|print
operator|.
name|PrintServiceLookup
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|print
operator|.
name|attribute
operator|.
name|Attribute
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|print
operator|.
name|attribute
operator|.
name|PrintRequestAttributeSet
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|print
operator|.
name|attribute
operator|.
name|standard
operator|.
name|Media
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|print
operator|.
name|attribute
operator|.
name|standard
operator|.
name|MediaSizeName
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|print
operator|.
name|attribute
operator|.
name|standard
operator|.
name|MediaTray
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|print
operator|.
name|attribute
operator|.
name|standard
operator|.
name|Sides
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
name|Message
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
name|test
operator|.
name|junit4
operator|.
name|CamelTestSupport
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
name|IOHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Ignore
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

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Matchers
operator|.
name|any
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Mockito
operator|.
name|mock
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Mockito
operator|.
name|times
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Mockito
operator|.
name|verify
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Mockito
operator|.
name|when
import|;
end_import

begin_class
DECL|class|PrinterPrintTest
specifier|public
class|class
name|PrinterPrintTest
extends|extends
name|CamelTestSupport
block|{
annotation|@
name|Before
DECL|method|setup ()
specifier|public
name|void
name|setup
parameter_list|()
block|{
name|setupJavaPrint
argument_list|()
expr_stmt|;
block|}
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
comment|// Check if there is an awt library
DECL|method|isAwtHeadless ()
specifier|private
name|boolean
name|isAwtHeadless
parameter_list|()
block|{
return|return
name|Boolean
operator|.
name|getBoolean
argument_list|(
literal|"java.awt.headless"
argument_list|)
return|;
block|}
DECL|method|sendFile ()
specifier|private
name|void
name|sendFile
parameter_list|()
throws|throws
name|Exception
block|{
name|template
operator|.
name|send
argument_list|(
literal|"direct:start"
argument_list|,
operator|new
name|Processor
argument_list|()
block|{
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
comment|// Read from an input stream
name|InputStream
name|is
init|=
name|IOHelper
operator|.
name|buffered
argument_list|(
operator|new
name|FileInputStream
argument_list|(
literal|"src/test/resources/test.txt"
argument_list|)
argument_list|)
decl_stmt|;
name|byte
name|buffer
index|[]
init|=
operator|new
name|byte
index|[
name|is
operator|.
name|available
argument_list|()
index|]
decl_stmt|;
name|int
name|n
init|=
name|is
operator|.
name|available
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|n
condition|;
name|i
operator|++
control|)
block|{
name|buffer
index|[
name|i
index|]
operator|=
operator|(
name|byte
operator|)
name|is
operator|.
name|read
argument_list|()
expr_stmt|;
block|}
name|is
operator|.
name|close
argument_list|()
expr_stmt|;
comment|// Set the property of the charset encoding
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|CHARSET_NAME
argument_list|,
literal|"UTF-8"
argument_list|)
expr_stmt|;
name|Message
name|in
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|in
operator|.
name|setBody
argument_list|(
name|buffer
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
DECL|method|sendGIF ()
specifier|private
name|void
name|sendGIF
parameter_list|()
throws|throws
name|Exception
block|{
name|template
operator|.
name|send
argument_list|(
literal|"direct:start"
argument_list|,
operator|new
name|Processor
argument_list|()
block|{
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
comment|// Read from an input stream
name|InputStream
name|is
init|=
name|IOHelper
operator|.
name|buffered
argument_list|(
operator|new
name|FileInputStream
argument_list|(
literal|"src/test/resources/asf-logo.gif"
argument_list|)
argument_list|)
decl_stmt|;
name|byte
name|buffer
index|[]
init|=
operator|new
name|byte
index|[
name|is
operator|.
name|available
argument_list|()
index|]
decl_stmt|;
name|int
name|n
init|=
name|is
operator|.
name|available
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|n
condition|;
name|i
operator|++
control|)
block|{
name|buffer
index|[
name|i
index|]
operator|=
operator|(
name|byte
operator|)
name|is
operator|.
name|read
argument_list|()
expr_stmt|;
block|}
name|is
operator|.
name|close
argument_list|()
expr_stmt|;
comment|// Set the property of the charset encoding
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|CHARSET_NAME
argument_list|,
literal|"UTF-8"
argument_list|)
expr_stmt|;
name|Message
name|in
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|in
operator|.
name|setBody
argument_list|(
name|buffer
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
DECL|method|sendJPEG ()
specifier|private
name|void
name|sendJPEG
parameter_list|()
throws|throws
name|Exception
block|{
name|template
operator|.
name|send
argument_list|(
literal|"direct:start"
argument_list|,
operator|new
name|Processor
argument_list|()
block|{
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
comment|// Read from an input stream
name|InputStream
name|is
init|=
name|IOHelper
operator|.
name|buffered
argument_list|(
operator|new
name|FileInputStream
argument_list|(
literal|"src/test/resources/asf-logo.JPG"
argument_list|)
argument_list|)
decl_stmt|;
name|byte
name|buffer
index|[]
init|=
operator|new
name|byte
index|[
name|is
operator|.
name|available
argument_list|()
index|]
decl_stmt|;
name|int
name|n
init|=
name|is
operator|.
name|available
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|n
condition|;
name|i
operator|++
control|)
block|{
name|buffer
index|[
name|i
index|]
operator|=
operator|(
name|byte
operator|)
name|is
operator|.
name|read
argument_list|()
expr_stmt|;
block|}
name|is
operator|.
name|close
argument_list|()
expr_stmt|;
comment|// Set the property of the charset encoding
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|CHARSET_NAME
argument_list|,
literal|"UTF-8"
argument_list|)
expr_stmt|;
name|Message
name|in
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|in
operator|.
name|setBody
argument_list|(
name|buffer
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
annotation|@
name|Ignore
DECL|method|testSendingFileToPrinter ()
specifier|public
name|void
name|testSendingFileToPrinter
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|isAwtHeadless
argument_list|()
condition|)
block|{
return|return;
block|}
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
name|to
argument_list|(
literal|"lpr://localhost/default?copies=1&flavor=DocFlavor.BYTE_ARRAY&mimeType=AUTOSENSE&mediaSize=na-letter&sides=one-sided&sendToPrinter=false"
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
name|sendFile
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
annotation|@
name|Ignore
DECL|method|testSendingGIFToPrinter ()
specifier|public
name|void
name|testSendingGIFToPrinter
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|isAwtHeadless
argument_list|()
condition|)
block|{
return|return;
block|}
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
name|to
argument_list|(
literal|"lpr://localhost/default?flavor=DocFlavor.INPUT_STREAM&mimeType=GIF&mediaSize=na-letter&sides=one-sided&sendToPrinter=false"
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
name|sendGIF
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
annotation|@
name|Ignore
DECL|method|testSendingJPEGToPrinter ()
specifier|public
name|void
name|testSendingJPEGToPrinter
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|isAwtHeadless
argument_list|()
condition|)
block|{
return|return;
block|}
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
name|to
argument_list|(
literal|"lpr://localhost/default?copies=2&flavor=DocFlavor.INPUT_STREAM"
operator|+
literal|"&mimeType=JPEG&mediaSize=na-letter&sides=one-sided&sendToPrinter=false"
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
name|sendJPEG
argument_list|()
expr_stmt|;
block|}
comment|/**      * Test for resolution of bug CAMEL-3446.      * Not specifying mediaSize nor sides attributes make it use      * default values when starting the route.      */
annotation|@
name|Test
annotation|@
name|Ignore
DECL|method|testDefaultPrinterConfiguration ()
specifier|public
name|void
name|testDefaultPrinterConfiguration
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|isAwtHeadless
argument_list|()
condition|)
block|{
return|return;
block|}
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
name|to
argument_list|(
literal|"lpr://localhost/default?sendToPrinter=false"
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
block|}
annotation|@
name|Test
DECL|method|moreThanOneLprEndpoint ()
specifier|public
name|void
name|moreThanOneLprEndpoint
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|isAwtHeadless
argument_list|()
condition|)
block|{
return|return;
block|}
name|int
name|numberOfPrintservicesBefore
init|=
name|PrintServiceLookup
operator|.
name|lookupPrintServices
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|)
operator|.
name|length
decl_stmt|;
comment|// setup javax.print
name|PrintService
name|ps1
init|=
name|mock
argument_list|(
name|PrintService
operator|.
name|class
argument_list|)
decl_stmt|;
name|when
argument_list|(
name|ps1
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
literal|"printer1"
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|ps1
operator|.
name|isDocFlavorSupported
argument_list|(
name|any
argument_list|(
name|DocFlavor
operator|.
name|class
argument_list|)
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
name|PrintService
name|ps2
init|=
name|mock
argument_list|(
name|PrintService
operator|.
name|class
argument_list|)
decl_stmt|;
name|when
argument_list|(
name|ps2
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
literal|"printer2"
argument_list|)
expr_stmt|;
name|boolean
name|res1
init|=
name|PrintServiceLookup
operator|.
name|registerService
argument_list|(
name|ps1
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"PrintService #1 should be registered."
argument_list|,
name|res1
argument_list|)
expr_stmt|;
name|boolean
name|res2
init|=
name|PrintServiceLookup
operator|.
name|registerService
argument_list|(
name|ps2
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"PrintService #2 should be registered."
argument_list|,
name|res2
argument_list|)
expr_stmt|;
name|PrintService
index|[]
name|pss
init|=
name|PrintServiceLookup
operator|.
name|lookupPrintServices
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"lookup should report two PrintServices."
argument_list|,
name|numberOfPrintservicesBefore
operator|+
literal|2
argument_list|,
name|pss
operator|.
name|length
argument_list|)
expr_stmt|;
name|DocPrintJob
name|job1
init|=
name|mock
argument_list|(
name|DocPrintJob
operator|.
name|class
argument_list|)
decl_stmt|;
name|when
argument_list|(
name|ps1
operator|.
name|createPrintJob
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|job1
argument_list|)
expr_stmt|;
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
literal|"direct:start1"
argument_list|)
operator|.
name|to
argument_list|(
literal|"lpr://localhost/printer1?sendToPrinter=true"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:start2"
argument_list|)
operator|.
name|to
argument_list|(
literal|"lpr://localhost/printer2?sendToPrinter=false"
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
comment|// Are there two different PrintConfigurations?
name|Map
argument_list|<
name|String
argument_list|,
name|Endpoint
argument_list|>
name|epm
init|=
name|context
argument_list|()
operator|.
name|getEndpointMap
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Four endpoints"
argument_list|,
literal|4
argument_list|,
name|epm
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Endpoint
name|lp1
init|=
literal|null
decl_stmt|;
name|Endpoint
name|lp2
init|=
literal|null
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Endpoint
argument_list|>
name|ep
range|:
name|epm
operator|.
name|entrySet
argument_list|()
control|)
block|{
if|if
condition|(
name|ep
operator|.
name|getKey
argument_list|()
operator|.
name|contains
argument_list|(
literal|"printer1"
argument_list|)
condition|)
block|{
name|lp1
operator|=
name|ep
operator|.
name|getValue
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|ep
operator|.
name|getKey
argument_list|()
operator|.
name|contains
argument_list|(
literal|"printer2"
argument_list|)
condition|)
block|{
name|lp2
operator|=
name|ep
operator|.
name|getValue
argument_list|()
expr_stmt|;
block|}
block|}
name|assertNotNull
argument_list|(
name|lp1
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|lp2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"printer1"
argument_list|,
operator|(
operator|(
name|PrinterEndpoint
operator|)
name|lp1
operator|)
operator|.
name|getConfig
argument_list|()
operator|.
name|getPrintername
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"printer2"
argument_list|,
operator|(
operator|(
name|PrinterEndpoint
operator|)
name|lp2
operator|)
operator|.
name|getConfig
argument_list|()
operator|.
name|getPrintername
argument_list|()
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start1"
argument_list|,
literal|"Hello Printer 1"
argument_list|)
expr_stmt|;
name|context
operator|.
name|stop
argument_list|()
expr_stmt|;
name|verify
argument_list|(
name|job1
argument_list|,
name|times
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|print
argument_list|(
name|any
argument_list|(
name|Doc
operator|.
name|class
argument_list|)
argument_list|,
name|any
argument_list|(
name|PrintRequestAttributeSet
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|printerNameTest ()
specifier|public
name|void
name|printerNameTest
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|isAwtHeadless
argument_list|()
condition|)
block|{
return|return;
block|}
comment|// setup javax.print
name|PrintService
name|ps1
init|=
name|mock
argument_list|(
name|PrintService
operator|.
name|class
argument_list|)
decl_stmt|;
name|when
argument_list|(
name|ps1
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
literal|"MyPrinter\\\\remote\\printer1"
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|ps1
operator|.
name|isDocFlavorSupported
argument_list|(
name|any
argument_list|(
name|DocFlavor
operator|.
name|class
argument_list|)
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
name|boolean
name|res1
init|=
name|PrintServiceLookup
operator|.
name|registerService
argument_list|(
name|ps1
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"The Remote PrintService #1 should be registered."
argument_list|,
name|res1
argument_list|)
expr_stmt|;
name|DocPrintJob
name|job1
init|=
name|mock
argument_list|(
name|DocPrintJob
operator|.
name|class
argument_list|)
decl_stmt|;
name|when
argument_list|(
name|ps1
operator|.
name|createPrintJob
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|job1
argument_list|)
expr_stmt|;
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
literal|"direct:start1"
argument_list|)
operator|.
name|to
argument_list|(
literal|"lpr://remote/printer1?sendToPrinter=true"
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
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start1"
argument_list|,
literal|"Hello Printer 1"
argument_list|)
expr_stmt|;
name|context
operator|.
name|stop
argument_list|()
expr_stmt|;
name|verify
argument_list|(
name|job1
argument_list|,
name|times
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|print
argument_list|(
name|any
argument_list|(
name|Doc
operator|.
name|class
argument_list|)
argument_list|,
name|any
argument_list|(
name|PrintRequestAttributeSet
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|setJobName ()
specifier|public
name|void
name|setJobName
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|isAwtHeadless
argument_list|()
condition|)
block|{
return|return;
block|}
name|getMockEndpoint
argument_list|(
literal|"mock:output"
argument_list|)
operator|.
name|setExpectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
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
name|to
argument_list|(
literal|"lpr://localhost/default"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:output"
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
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:start"
argument_list|,
literal|"Hello Printer"
argument_list|,
name|PrinterEndpoint
operator|.
name|JOB_NAME
argument_list|,
literal|"Test-Job-Name"
argument_list|)
expr_stmt|;
name|context
operator|.
name|stop
argument_list|()
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|printToMiddleTray ()
specifier|public
name|void
name|printToMiddleTray
parameter_list|()
throws|throws
name|Exception
block|{
name|PrinterEndpoint
name|endpoint
init|=
operator|new
name|PrinterEndpoint
argument_list|()
decl_stmt|;
name|PrinterConfiguration
name|configuration
init|=
operator|new
name|PrinterConfiguration
argument_list|()
decl_stmt|;
name|configuration
operator|.
name|setHostname
argument_list|(
literal|"localhost"
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|setPort
argument_list|(
literal|631
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|setPrintername
argument_list|(
literal|"DefaultPrinter"
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|setMediaSizeName
argument_list|(
name|MediaSizeName
operator|.
name|ISO_A4
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|setInternalSides
argument_list|(
name|Sides
operator|.
name|ONE_SIDED
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|setMediaTray
argument_list|(
literal|"middle"
argument_list|)
expr_stmt|;
name|PrinterProducer
name|producer
init|=
operator|new
name|PrinterProducer
argument_list|(
name|endpoint
argument_list|,
name|configuration
argument_list|)
decl_stmt|;
name|producer
operator|.
name|start
argument_list|()
expr_stmt|;
name|PrinterOperations
name|printerOperations
init|=
name|producer
operator|.
name|getPrinterOperations
argument_list|()
decl_stmt|;
name|PrintRequestAttributeSet
name|attributeSet
init|=
name|printerOperations
operator|.
name|getPrintRequestAttributeSet
argument_list|()
decl_stmt|;
name|Attribute
name|attribute
init|=
name|attributeSet
operator|.
name|get
argument_list|(
name|javax
operator|.
name|print
operator|.
name|attribute
operator|.
name|standard
operator|.
name|Media
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|attribute
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|attribute
operator|instanceof
name|MediaTray
argument_list|)
expr_stmt|;
name|MediaTray
name|mediaTray
init|=
operator|(
name|MediaTray
operator|)
name|attribute
decl_stmt|;
name|assertEquals
argument_list|(
literal|"middle"
argument_list|,
name|mediaTray
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|setupJavaPrint ()
specifier|protected
name|void
name|setupJavaPrint
parameter_list|()
block|{
comment|// "install" another default printer
name|PrintService
name|psDefault
init|=
name|mock
argument_list|(
name|PrintService
operator|.
name|class
argument_list|)
decl_stmt|;
name|when
argument_list|(
name|psDefault
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
literal|"DefaultPrinter"
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|psDefault
operator|.
name|isDocFlavorSupported
argument_list|(
name|any
argument_list|(
name|DocFlavor
operator|.
name|class
argument_list|)
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
name|PrintServiceLookup
name|psLookup
init|=
name|mock
argument_list|(
name|PrintServiceLookup
operator|.
name|class
argument_list|)
decl_stmt|;
name|when
argument_list|(
name|psLookup
operator|.
name|getPrintServices
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
operator|new
name|PrintService
index|[]
block|{
name|psDefault
block|}
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|psLookup
operator|.
name|getDefaultPrintService
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|psDefault
argument_list|)
expr_stmt|;
name|DocPrintJob
name|docPrintJob
init|=
name|mock
argument_list|(
name|DocPrintJob
operator|.
name|class
argument_list|)
decl_stmt|;
name|when
argument_list|(
name|psDefault
operator|.
name|createPrintJob
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|docPrintJob
argument_list|)
expr_stmt|;
name|MediaTray
index|[]
name|trays
init|=
operator|new
name|MediaTray
index|[]
block|{
name|MediaTray
operator|.
name|TOP
block|,
name|MediaTray
operator|.
name|MIDDLE
block|,
name|MediaTray
operator|.
name|BOTTOM
block|}
decl_stmt|;
name|when
argument_list|(
name|psDefault
operator|.
name|getSupportedAttributeValues
argument_list|(
name|Media
operator|.
name|class
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|trays
argument_list|)
expr_stmt|;
name|PrintServiceLookup
operator|.
name|registerServiceProvider
argument_list|(
name|psLookup
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

