begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.file
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|file
package|;
end_package

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
name|TestSupport
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
name|CamelTemplate
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
name|log4j
operator|.
name|Logger
import|;
end_import

begin_comment
comment|/**  * @author Albert Moraal  * @version $Revision$  */
end_comment

begin_class
DECL|class|DirectoryCreateIssueTest
specifier|public
class|class
name|DirectoryCreateIssueTest
extends|extends
name|TestSupport
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|Logger
operator|.
name|getLogger
argument_list|(
name|DirectoryCreateIssueTest
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|context
specifier|private
name|CamelContext
name|context
decl_stmt|;
DECL|field|template
specifier|private
name|CamelTemplate
name|template
decl_stmt|;
annotation|@
name|Override
DECL|method|setUp ()
specifier|protected
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
comment|// The following code is removed
comment|// if you want to enable debugging, add the src/test/ide-resources directory to your IDE classpath
comment|/*         BasicConfigurator.configure();         Logger.getRootLogger().setLevel(Level.DEBUG); */
name|context
operator|=
operator|new
name|DefaultCamelContext
argument_list|()
expr_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|template
operator|=
operator|new
name|CamelTemplate
argument_list|(
name|context
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|tearDown ()
specifier|protected
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
name|context
operator|.
name|stop
argument_list|()
expr_stmt|;
name|super
operator|.
name|tearDown
argument_list|()
expr_stmt|;
block|}
DECL|method|testFileCreatedAsDir ()
specifier|public
name|void
name|testFileCreatedAsDir
parameter_list|()
throws|throws
name|Exception
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"testFileCreatedAsDir"
argument_list|)
expr_stmt|;
specifier|final
name|String
name|path
init|=
literal|"target/a/b/c/d/e/f/g/h"
decl_stmt|;
specifier|final
name|int
name|numFiles
init|=
literal|10
decl_stmt|;
name|context
operator|.
name|addRoutes
argument_list|(
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
block|{
name|String
index|[]
name|destinations
init|=
operator|new
name|String
index|[
name|numFiles
index|]
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
name|numFiles
condition|;
name|i
operator|++
control|)
block|{
name|destinations
index|[
name|i
index|]
operator|=
literal|"seda:file"
operator|+
name|i
expr_stmt|;
name|from
argument_list|(
literal|"seda:file"
operator|+
name|i
argument_list|)
operator|.
name|setHeader
argument_list|(
name|FileComponent
operator|.
name|HEADER_FILE_NAME
argument_list|,
name|constant
argument_list|(
literal|"file"
operator|+
name|i
operator|+
literal|".txt"
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"file://"
operator|+
name|path
operator|+
literal|"/?append=false&noop=true"
argument_list|)
expr_stmt|;
block|}
name|from
argument_list|(
literal|"seda:testFileCreatedAsDir"
argument_list|)
operator|.
name|to
argument_list|(
name|destinations
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|deleteDirectory
argument_list|(
operator|new
name|File
argument_list|(
literal|"a"
argument_list|)
argument_list|)
expr_stmt|;
name|template
operator|.
name|send
argument_list|(
literal|"seda:testFileCreatedAsDir"
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
block|{
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
literal|"Contents of test file"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|8
operator|*
literal|1000
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|numFiles
condition|;
name|i
operator|++
control|)
block|{
name|assertTrue
argument_list|(
operator|(
operator|new
name|File
argument_list|(
name|path
operator|+
literal|"/file"
operator|+
name|i
operator|+
literal|".txt"
argument_list|)
operator|)
operator|.
name|isFile
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

