begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.hdfs.osgi
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|hdfs
operator|.
name|osgi
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|hadoop
operator|.
name|util
operator|.
name|ShutdownHookManager
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|BundleActivator
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|BundleContext
import|;
end_import

begin_class
DECL|class|HdfsActivator
specifier|public
class|class
name|HdfsActivator
implements|implements
name|BundleActivator
block|{
annotation|@
name|Override
DECL|method|start (BundleContext context)
specifier|public
name|void
name|start
parameter_list|(
name|BundleContext
name|context
parameter_list|)
throws|throws
name|Exception
block|{     }
annotation|@
name|Override
DECL|method|stop (BundleContext context)
specifier|public
name|void
name|stop
parameter_list|(
name|BundleContext
name|context
parameter_list|)
throws|throws
name|Exception
block|{
comment|// There's problem inside OSGi when framwork is being shutdown
comment|// hadoop.fs code registers some JVM shutdown hooks throughout the code and this ordered
comment|// list of hooks is run in shutdown thread.
comment|// At that time bundle class loader / bundle wiring is no longer valid (bundle is stopped)
comment|// so ShutdownHookManager can't load additional classes. But there are some inner classes
comment|// loaded when iterating over registered hadoop shutdown hooks.
comment|// Let's explicitely load these inner classes when bundle is stopped, as there's last chance
comment|// to use valid bundle class loader.
comment|// This is based on the knowledge of what's contained in SMX bundle
comment|// org.apache.servicemix.bundles.hadoop-client-*.jar
comment|// the above is just a warning that hadopp may have some quirks when running inside OSGi
name|ClassLoader
name|hadoopCl
init|=
name|ShutdownHookManager
operator|.
name|class
operator|.
name|getClassLoader
argument_list|()
decl_stmt|;
if|if
condition|(
name|hadoopCl
operator|!=
literal|null
condition|)
block|{
name|String
name|shm
init|=
name|ShutdownHookManager
operator|.
name|class
operator|.
name|getName
argument_list|()
decl_stmt|;
name|hadoopCl
operator|.
name|loadClass
argument_list|(
name|shm
operator|+
literal|"$1"
argument_list|)
expr_stmt|;
name|hadoopCl
operator|.
name|loadClass
argument_list|(
name|shm
operator|+
literal|"$2"
argument_list|)
expr_stmt|;
name|hadoopCl
operator|.
name|loadClass
argument_list|(
name|shm
operator|+
literal|"$HookEntry"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

