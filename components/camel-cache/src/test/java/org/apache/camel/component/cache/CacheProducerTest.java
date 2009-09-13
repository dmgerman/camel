begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cache
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|cache
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|BufferedInputStream
import|;
end_import

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
name|converter
operator|.
name|IOConverter
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
name|junit
operator|.
name|Test
import|;
end_import

begin_class
DECL|class|CacheProducerTest
specifier|public
class|class
name|CacheProducerTest
extends|extends
name|CamelTestSupport
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
name|CacheProducerTest
operator|.
name|class
argument_list|)
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
operator|new
name|BufferedInputStream
argument_list|(
operator|new
name|FileInputStream
argument_list|(
literal|"./src/test/resources/test.txt"
argument_list|)
argument_list|)
decl_stmt|;
name|byte
name|buffer
index|[]
init|=
name|IOConverter
operator|.
name|toBytes
argument_list|(
name|is
argument_list|)
decl_stmt|;
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
DECL|method|sendUpdatedFile ()
specifier|private
name|void
name|sendUpdatedFile
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
operator|new
name|BufferedInputStream
argument_list|(
operator|new
name|FileInputStream
argument_list|(
literal|"./src/test/resources/updatedtest.txt"
argument_list|)
argument_list|)
decl_stmt|;
name|byte
name|buffer
index|[]
init|=
name|IOConverter
operator|.
name|toBytes
argument_list|(
name|is
argument_list|)
decl_stmt|;
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
DECL|method|testAddingDataToCache ()
specifier|public
name|void
name|testAddingDataToCache
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
name|setHeader
argument_list|(
literal|"CACHE_OPERATION"
argument_list|,
name|constant
argument_list|(
literal|"ADD"
argument_list|)
argument_list|)
operator|.
name|setHeader
argument_list|(
literal|"CACHE_KEY"
argument_list|,
name|constant
argument_list|(
literal|"Ralph_Waldo_Emerson"
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"cache://TestCache1"
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
name|LOG
operator|.
name|info
argument_list|(
literal|"------------Beginning CacheProducer Add Test---------------"
argument_list|)
expr_stmt|;
name|sendFile
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testUpdatingDataInCache ()
specifier|public
name|void
name|testUpdatingDataInCache
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
name|setHeader
argument_list|(
literal|"CACHE_OPERATION"
argument_list|,
name|constant
argument_list|(
literal|"UPDATE"
argument_list|)
argument_list|)
operator|.
name|setHeader
argument_list|(
literal|"CACHE_KEY"
argument_list|,
name|constant
argument_list|(
literal|"Ralph_Waldo_Emerson"
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"cache://TestCache1"
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
name|LOG
operator|.
name|info
argument_list|(
literal|"------------Beginning CacheProducer Update Test---------------"
argument_list|)
expr_stmt|;
name|sendUpdatedFile
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testDeletingDataFromCache ()
specifier|public
name|void
name|testDeletingDataFromCache
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
name|setHeader
argument_list|(
literal|"CACHE_OPERATION"
argument_list|,
name|constant
argument_list|(
literal|"DELETE"
argument_list|)
argument_list|)
operator|.
name|setHeader
argument_list|(
literal|"CACHE_KEY"
argument_list|,
name|constant
argument_list|(
literal|"Ralph_Waldo_Emerson"
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"cache://TestCache1"
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
name|LOG
operator|.
name|info
argument_list|(
literal|"------------Beginning CacheProducer Delete Test---------------"
argument_list|)
expr_stmt|;
name|sendUpdatedFile
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testDeletingAllDataFromCache ()
specifier|public
name|void
name|testDeletingAllDataFromCache
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
name|setHeader
argument_list|(
literal|"CACHE_OPERATION"
argument_list|,
name|constant
argument_list|(
literal|"ADD"
argument_list|)
argument_list|)
operator|.
name|setHeader
argument_list|(
literal|"CACHE_KEY"
argument_list|,
name|constant
argument_list|(
literal|"Ralph_Waldo_Emerson"
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"cache://TestCache1"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|setHeader
argument_list|(
literal|"CACHE_OPERATION"
argument_list|,
name|constant
argument_list|(
literal|"ADD"
argument_list|)
argument_list|)
operator|.
name|setHeader
argument_list|(
literal|"CACHE_KEY"
argument_list|,
name|constant
argument_list|(
literal|"Ralph_Waldo_Emerson2"
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"cache://TestCache1"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|setHeader
argument_list|(
literal|"CACHE_OPERATION"
argument_list|,
name|constant
argument_list|(
literal|"DELETEALL"
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"cache://TestCache1"
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
name|LOG
operator|.
name|info
argument_list|(
literal|"------------Beginning CacheProducer Delete All Elements Test---------------"
argument_list|)
expr_stmt|;
name|sendUpdatedFile
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

