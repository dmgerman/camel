begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileOutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeUnit
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
name|CamelException
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
name|builder
operator|.
name|NotifyBuilder
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
name|util
operator|.
name|IOHelper
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
name|StopWatch
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
name|TimeUtils
import|;
end_import

begin_comment
comment|/**  * @version   */
end_comment

begin_class
DECL|class|SplitterParallelBigFileTest
specifier|public
class|class
name|SplitterParallelBigFileTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|lines
specifier|private
name|int
name|lines
init|=
literal|20000
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
name|deleteDirectory
argument_list|(
literal|"target/split"
argument_list|)
expr_stmt|;
name|createDirectory
argument_list|(
literal|"target/split"
argument_list|)
expr_stmt|;
name|createBigFile
argument_list|()
expr_stmt|;
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
block|}
DECL|method|createBigFile ()
specifier|private
name|void
name|createBigFile
parameter_list|()
throws|throws
name|Exception
block|{
name|File
name|file
init|=
operator|new
name|File
argument_list|(
literal|"target/split/bigfile.txt"
argument_list|)
decl_stmt|;
name|FileOutputStream
name|fos
init|=
operator|new
name|FileOutputStream
argument_list|(
name|file
argument_list|)
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
name|lines
condition|;
name|i
operator|++
control|)
block|{
name|String
name|line
init|=
literal|"line-"
operator|+
name|i
operator|+
name|LS
decl_stmt|;
name|fos
operator|.
name|write
argument_list|(
name|line
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|IOHelper
operator|.
name|close
argument_list|(
name|fos
argument_list|)
expr_stmt|;
block|}
DECL|method|testNoop ()
specifier|public
name|void
name|testNoop
parameter_list|()
block|{
comment|// noop
block|}
comment|// disabled due manual test
DECL|method|xxxtestSplitParallelBigFile ()
specifier|public
name|void
name|xxxtestSplitParallelBigFile
parameter_list|()
throws|throws
name|Exception
block|{
name|StopWatch
name|watch
init|=
operator|new
name|StopWatch
argument_list|()
decl_stmt|;
name|NotifyBuilder
name|builder
init|=
operator|new
name|NotifyBuilder
argument_list|(
name|context
argument_list|)
operator|.
name|whenDone
argument_list|(
name|lines
operator|+
literal|1
argument_list|)
operator|.
name|create
argument_list|()
decl_stmt|;
name|boolean
name|done
init|=
name|builder
operator|.
name|matches
argument_list|(
literal|120
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
decl_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"Took "
operator|+
name|TimeUtils
operator|.
name|printDuration
argument_list|(
name|watch
operator|.
name|taken
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|done
condition|)
block|{
throw|throw
operator|new
name|CamelException
argument_list|(
literal|"Could not split file in 2 minutes"
argument_list|)
throw|;
block|}
comment|// need a little sleep for capturing memory profiling
comment|// Thread.sleep(60 * 1000);
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
comment|// lower max pool to 10 for less number of concurrent threads
comment|//context.getExecutorServiceStrategy().getDefaultThreadPoolProfile().setMaxPoolSize(10);
name|from
argument_list|(
literal|"file:target/split"
argument_list|)
operator|.
name|split
argument_list|(
name|body
argument_list|()
operator|.
name|tokenize
argument_list|(
name|LS
argument_list|)
argument_list|)
operator|.
name|streaming
argument_list|()
operator|.
name|parallelProcessing
argument_list|()
operator|.
name|to
argument_list|(
literal|"log:split?groupSize=1000"
argument_list|)
operator|.
name|end
argument_list|()
operator|.
name|log
argument_list|(
literal|"Done splitting ${file:name}"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

