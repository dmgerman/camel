begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.exec
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|exec
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|io
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
name|commons
operator|.
name|io
operator|.
name|LineIterator
import|;
end_import

begin_comment
comment|/**  * A test Java main class to be executed. The behavior of the program is  * controlled by the arguments that the {@link #main(String[])} receives. Valid  * arguments are the public static fields of the class.  */
end_comment

begin_class
DECL|class|ExecutableJavaProgram
specifier|public
class|class
name|ExecutableJavaProgram
block|{
comment|/**      * Start 2 threads that print text in the stdout and stderr, each printing      * {@link #LINES_TO_PRINT_FROM_EACH_THREAD} lines.      */
DECL|field|THREADS
specifier|public
specifier|static
specifier|final
name|String
name|THREADS
init|=
literal|"threads"
decl_stmt|;
DECL|field|SLEEP_WITH_TIMEOUT
specifier|public
specifier|static
specifier|final
name|String
name|SLEEP_WITH_TIMEOUT
init|=
literal|"timeout"
decl_stmt|;
DECL|field|SLEEP_TIME
specifier|public
specifier|static
specifier|final
name|int
name|SLEEP_TIME
init|=
literal|60
operator|*
literal|1000
decl_stmt|;
DECL|field|PRINT_IN_STDOUT
specifier|public
specifier|static
specifier|final
name|String
name|PRINT_IN_STDOUT
init|=
literal|"print.in.stdout"
decl_stmt|;
DECL|field|PRINT_ARGS_STDOUT
specifier|public
specifier|static
specifier|final
name|String
name|PRINT_ARGS_STDOUT
init|=
literal|"print.args.stdout"
decl_stmt|;
DECL|field|PRINT_IN_STDERR
specifier|public
specifier|static
specifier|final
name|String
name|PRINT_IN_STDERR
init|=
literal|"print.in.stderr"
decl_stmt|;
DECL|field|READ_INPUT_LINES_AND_PRINT_THEM
specifier|public
specifier|static
specifier|final
name|String
name|READ_INPUT_LINES_AND_PRINT_THEM
init|=
literal|"read.input.lines.and.print.them"
decl_stmt|;
DECL|field|EXIT_WITH_VALUE_0
specifier|public
specifier|static
specifier|final
name|int
name|EXIT_WITH_VALUE_0
init|=
literal|0
decl_stmt|;
DECL|field|EXIT_WITH_VALUE_1
specifier|public
specifier|static
specifier|final
name|int
name|EXIT_WITH_VALUE_1
init|=
literal|1
decl_stmt|;
DECL|field|LINES_TO_PRINT_FROM_EACH_THREAD
specifier|public
specifier|static
specifier|final
name|int
name|LINES_TO_PRINT_FROM_EACH_THREAD
init|=
literal|50
decl_stmt|;
DECL|method|ExecutableJavaProgram ()
specifier|protected
name|ExecutableJavaProgram
parameter_list|()
block|{      }
DECL|method|main (String[] args)
specifier|public
specifier|static
name|void
name|main
parameter_list|(
name|String
index|[]
name|args
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|args
operator|==
literal|null
operator|||
name|args
operator|.
name|length
operator|==
literal|0
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Empty args are not allowed."
argument_list|)
throw|;
block|}
if|if
condition|(
name|args
index|[
literal|0
index|]
operator|.
name|equals
argument_list|(
name|PRINT_IN_STDOUT
argument_list|)
condition|)
block|{
name|System
operator|.
name|out
operator|.
name|print
argument_list|(
name|PRINT_IN_STDOUT
argument_list|)
expr_stmt|;
name|System
operator|.
name|exit
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|args
index|[
literal|0
index|]
operator|.
name|equals
argument_list|(
name|PRINT_ARGS_STDOUT
argument_list|)
condition|)
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|args
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|String
name|arg
init|=
name|args
index|[
name|i
index|]
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|i
operator|+
name|arg
argument_list|)
expr_stmt|;
block|}
name|System
operator|.
name|exit
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|args
index|[
literal|0
index|]
operator|.
name|equals
argument_list|(
name|PRINT_IN_STDERR
argument_list|)
condition|)
block|{
name|System
operator|.
name|err
operator|.
name|print
argument_list|(
name|PRINT_IN_STDERR
argument_list|)
expr_stmt|;
name|System
operator|.
name|exit
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|args
index|[
literal|0
index|]
operator|.
name|equals
argument_list|(
name|String
operator|.
name|valueOf
argument_list|(
name|EXIT_WITH_VALUE_0
argument_list|)
argument_list|)
condition|)
block|{
name|System
operator|.
name|exit
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|args
index|[
literal|0
index|]
operator|.
name|equals
argument_list|(
name|String
operator|.
name|valueOf
argument_list|(
name|EXIT_WITH_VALUE_1
argument_list|)
argument_list|)
condition|)
block|{
name|System
operator|.
name|exit
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|args
index|[
literal|0
index|]
operator|.
name|equals
argument_list|(
name|THREADS
argument_list|)
condition|)
block|{
name|Thread
name|stderrPrinterThread
init|=
operator|new
name|Thread
argument_list|(
operator|new
name|ErrPrinter
argument_list|()
argument_list|)
decl_stmt|;
name|Thread
name|stdoutPrinterThread
init|=
operator|new
name|Thread
argument_list|(
operator|new
name|OutPrinter
argument_list|()
argument_list|)
decl_stmt|;
name|stderrPrinterThread
operator|.
name|start
argument_list|()
expr_stmt|;
name|stdoutPrinterThread
operator|.
name|start
argument_list|()
expr_stmt|;
name|stderrPrinterThread
operator|.
name|join
argument_list|()
expr_stmt|;
name|stdoutPrinterThread
operator|.
name|join
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|args
index|[
literal|0
index|]
operator|.
name|equals
argument_list|(
name|SLEEP_WITH_TIMEOUT
argument_list|)
condition|)
block|{
name|doSleep
argument_list|()
expr_stmt|;
name|System
operator|.
name|exit
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|READ_INPUT_LINES_AND_PRINT_THEM
operator|.
name|equals
argument_list|(
name|args
index|[
literal|0
index|]
argument_list|)
condition|)
block|{
name|LineIterator
name|iterator
init|=
name|IOUtils
operator|.
name|lineIterator
argument_list|(
name|System
operator|.
name|in
argument_list|,
literal|"UTF-8"
argument_list|)
decl_stmt|;
while|while
condition|(
name|iterator
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|String
name|line
init|=
name|iterator
operator|.
name|nextLine
argument_list|()
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|line
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|args
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|doSleep ()
specifier|private
specifier|static
name|void
name|doSleep
parameter_list|()
throws|throws
name|InterruptedException
block|{
name|int
name|sleepInterval
init|=
literal|50
decl_stmt|;
comment|// Note, that sleeping in the main thread prevents the process from
comment|// being destroyed for that time. The process is killed namely when
comment|// sleep returns(observed on Windows XP)
name|int
name|t
init|=
literal|0
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Sleeping every "
operator|+
name|String
operator|.
name|valueOf
argument_list|(
name|sleepInterval
argument_list|)
operator|+
literal|" ms"
argument_list|)
expr_stmt|;
for|for
control|(
init|;
name|t
operator|<
name|SLEEP_TIME
operator|%
name|sleepInterval
condition|;
name|t
operator|+=
name|sleepInterval
control|)
block|{
name|Thread
operator|.
name|sleep
argument_list|(
name|sleepInterval
argument_list|)
expr_stmt|;
block|}
block|}
DECL|class|ErrPrinter
specifier|private
specifier|static
class|class
name|ErrPrinter
implements|implements
name|Runnable
block|{
annotation|@
name|Override
DECL|method|run ()
specifier|public
name|void
name|run
parameter_list|()
block|{
for|for
control|(
name|int
name|t
init|=
literal|0
init|;
name|t
operator|<
name|LINES_TO_PRINT_FROM_EACH_THREAD
condition|;
name|t
operator|++
control|)
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
name|PRINT_IN_STDERR
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|class|OutPrinter
specifier|private
specifier|static
class|class
name|OutPrinter
implements|implements
name|Runnable
block|{
annotation|@
name|Override
DECL|method|run ()
specifier|public
name|void
name|run
parameter_list|()
block|{
for|for
control|(
name|int
name|t
init|=
literal|0
init|;
name|t
operator|<
name|LINES_TO_PRINT_FROM_EACH_THREAD
condition|;
name|t
operator|++
control|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|PRINT_IN_STDOUT
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

