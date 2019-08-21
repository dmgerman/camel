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
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FilterInputStream
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
name|impl
operator|.
name|DefaultCamelContext
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
name|ManagementNameStrategy
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
name|StreamCachingStrategy
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
name|Test
import|;
end_import

begin_class
DECL|class|StreamCachingSpoolDirectoryQuarkusTest
specifier|public
class|class
name|StreamCachingSpoolDirectoryQuarkusTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|spoolRule
specifier|private
name|MyCustomSpoolRule
name|spoolRule
init|=
operator|new
name|MyCustomSpoolRule
argument_list|()
decl_stmt|;
DECL|class|MyCamelContext
specifier|private
class|class
name|MyCamelContext
extends|extends
name|DefaultCamelContext
block|{
DECL|method|MyCamelContext (boolean init)
specifier|public
name|MyCamelContext
parameter_list|(
name|boolean
name|init
parameter_list|)
block|{
name|super
argument_list|(
name|init
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getManagementNameStrategy ()
specifier|public
name|ManagementNameStrategy
name|getManagementNameStrategy
parameter_list|()
block|{
comment|// quarkus has no management at all
return|return
literal|null
return|;
block|}
block|}
annotation|@
name|Override
annotation|@
name|Before
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|deleteDirectory
argument_list|(
literal|"target/data/cachedir"
argument_list|)
expr_stmt|;
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createCamelContext ()
specifier|protected
name|CamelContext
name|createCamelContext
parameter_list|()
throws|throws
name|Exception
block|{
name|DefaultCamelContext
name|context
init|=
operator|new
name|MyCamelContext
argument_list|(
literal|false
argument_list|)
decl_stmt|;
name|context
operator|.
name|disableJMX
argument_list|()
expr_stmt|;
name|context
operator|.
name|setRegistry
argument_list|(
name|createRegistry
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|setLoadTypeConverters
argument_list|(
name|isLoadTypeConverters
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|context
return|;
block|}
annotation|@
name|Test
DECL|method|testByteArrayInputStream ()
specifier|public
name|void
name|testByteArrayInputStream
parameter_list|()
throws|throws
name|Exception
block|{
name|getMockEndpoint
argument_list|(
literal|"mock:english"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"<hello/>"
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:dutch"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"<hallo/>"
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:german"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"<hallo/>"
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:french"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"<hellos/>"
argument_list|)
expr_stmt|;
comment|// need to wrap in MyInputStream as ByteArrayInputStream is optimized to
comment|// just reuse in memory buffer
comment|// and not needed to spool to disk
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:a"
argument_list|,
operator|new
name|MyInputStream
argument_list|(
operator|new
name|ByteArrayInputStream
argument_list|(
literal|"<hello/>"
operator|.
name|getBytes
argument_list|()
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|spoolRule
operator|.
name|setSpool
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:a"
argument_list|,
operator|new
name|MyInputStream
argument_list|(
operator|new
name|ByteArrayInputStream
argument_list|(
literal|"<hallo/>"
operator|.
name|getBytes
argument_list|()
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:a"
argument_list|,
operator|new
name|MyInputStream
argument_list|(
operator|new
name|ByteArrayInputStream
argument_list|(
literal|"<hellos/>"
operator|.
name|getBytes
argument_list|()
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
DECL|class|MyInputStream
specifier|private
specifier|final
class|class
name|MyInputStream
extends|extends
name|FilterInputStream
block|{
DECL|method|MyInputStream (InputStream in)
specifier|private
name|MyInputStream
parameter_list|(
name|InputStream
name|in
parameter_list|)
block|{
name|super
argument_list|(
name|in
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|RouteBuilder
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|context
operator|.
name|getStreamCachingStrategy
argument_list|()
operator|.
name|setSpoolDirectory
argument_list|(
literal|"target/cachedir"
argument_list|)
expr_stmt|;
name|context
operator|.
name|getStreamCachingStrategy
argument_list|()
operator|.
name|addSpoolRule
argument_list|(
name|spoolRule
argument_list|)
expr_stmt|;
name|context
operator|.
name|getStreamCachingStrategy
argument_list|()
operator|.
name|setAnySpoolRules
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|context
operator|.
name|setStreamCaching
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:a"
argument_list|)
operator|.
name|choice
argument_list|()
operator|.
name|when
argument_list|(
name|xpath
argument_list|(
literal|"//hello"
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:english"
argument_list|)
operator|.
name|when
argument_list|(
name|xpath
argument_list|(
literal|"//hallo"
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:dutch"
argument_list|,
literal|"mock:german"
argument_list|)
operator|.
name|otherwise
argument_list|()
operator|.
name|to
argument_list|(
literal|"mock:french"
argument_list|)
operator|.
name|end
argument_list|()
operator|.
name|process
argument_list|(
operator|new
name|Processor
argument_list|()
block|{
annotation|@
name|Override
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
comment|// check if spool file exists
if|if
condition|(
name|spoolRule
operator|.
name|isSpool
argument_list|()
condition|)
block|{
name|String
index|[]
name|names
init|=
operator|new
name|File
argument_list|(
literal|"target/cachedir"
argument_list|)
operator|.
name|list
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"There should be a cached spool file"
argument_list|,
literal|1
argument_list|,
name|names
operator|.
name|length
argument_list|)
expr_stmt|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|class|MyCustomSpoolRule
specifier|private
specifier|static
specifier|final
class|class
name|MyCustomSpoolRule
implements|implements
name|StreamCachingStrategy
operator|.
name|SpoolRule
block|{
DECL|field|spool
specifier|private
specifier|volatile
name|boolean
name|spool
decl_stmt|;
annotation|@
name|Override
DECL|method|shouldSpoolCache (long length)
specifier|public
name|boolean
name|shouldSpoolCache
parameter_list|(
name|long
name|length
parameter_list|)
block|{
return|return
name|spool
return|;
block|}
DECL|method|isSpool ()
specifier|public
name|boolean
name|isSpool
parameter_list|()
block|{
return|return
name|spool
return|;
block|}
DECL|method|setSpool (boolean spool)
specifier|public
name|void
name|setSpool
parameter_list|(
name|boolean
name|spool
parameter_list|)
block|{
name|this
operator|.
name|spool
operator|=
name|spool
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"MyCustomSpoolRule"
return|;
block|}
block|}
block|}
end_class

end_unit

