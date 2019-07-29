begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.opentracing
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|opentracing
package|;
end_package

begin_import
import|import
name|io
operator|.
name|opentracing
operator|.
name|noop
operator|.
name|NoopTracer
import|;
end_import

begin_import
import|import
name|io
operator|.
name|opentracing
operator|.
name|noop
operator|.
name|NoopTracerFactory
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
name|BindToRegistry
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
name|impl
operator|.
name|JndiRegistry
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
name|junit
operator|.
name|Test
import|;
end_import

begin_class
DECL|class|OpentracingSpanCollectorInRegistryTest
specifier|public
class|class
name|OpentracingSpanCollectorInRegistryTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|openTracing
specifier|private
name|OpenTracingTracer
name|openTracing
decl_stmt|;
annotation|@
name|BindToRegistry
argument_list|(
literal|"tracer"
argument_list|)
DECL|field|c
specifier|private
name|NoopTracer
name|c
init|=
name|NoopTracerFactory
operator|.
name|create
argument_list|()
decl_stmt|;
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
name|CamelContext
name|context
init|=
name|super
operator|.
name|createCamelContext
argument_list|()
decl_stmt|;
name|openTracing
operator|=
operator|new
name|OpenTracingTracer
argument_list|()
expr_stmt|;
name|openTracing
operator|.
name|init
argument_list|(
name|context
argument_list|)
expr_stmt|;
return|return
name|context
return|;
block|}
annotation|@
name|Test
DECL|method|testZipkinConfiguration ()
specifier|public
name|void
name|testZipkinConfiguration
parameter_list|()
throws|throws
name|Exception
block|{
name|assertNotNull
argument_list|(
name|openTracing
operator|.
name|getTracer
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|openTracing
operator|.
name|getTracer
argument_list|()
operator|instanceof
name|NoopTracer
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

