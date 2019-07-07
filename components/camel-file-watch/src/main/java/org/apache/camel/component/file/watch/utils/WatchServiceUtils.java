begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.file.watch.utils
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
operator|.
name|watch
operator|.
name|utils
package|;
end_package

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|WatchService
import|;
end_import

begin_class
DECL|class|WatchServiceUtils
specifier|public
class|class
name|WatchServiceUtils
block|{
DECL|method|WatchServiceUtils ()
specifier|private
name|WatchServiceUtils
parameter_list|()
block|{     }
comment|/**      * Check if @param watchService is underlying sun.nio.fs.PollingWatchService      * This can happen on OS X, AIX and Solaris prior to version 11      */
DECL|method|isPollingWatchService (WatchService watchService)
specifier|public
specifier|static
name|boolean
name|isPollingWatchService
parameter_list|(
name|WatchService
name|watchService
parameter_list|)
block|{
try|try
block|{
comment|// If the WatchService is a PollingWatchService, which it is on OS X, AIX and Solaris prior to version 11
name|Class
argument_list|<
name|?
argument_list|>
name|pollingWatchService
init|=
name|Class
operator|.
name|forName
argument_list|(
literal|"sun.nio.fs.PollingWatchService"
argument_list|)
decl_stmt|;
return|return
name|pollingWatchService
operator|.
name|isInstance
argument_list|(
name|watchService
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|ClassNotFoundException
name|ignored
parameter_list|)
block|{
comment|// This is expected on JVMs where PollingWatchService is not available
return|return
literal|false
return|;
block|}
block|}
block|}
end_class

end_unit

