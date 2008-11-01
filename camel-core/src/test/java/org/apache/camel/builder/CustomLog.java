begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.builder
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|builder
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
name|logging
operator|.
name|Log
import|;
end_import

begin_class
DECL|class|CustomLog
class|class
name|CustomLog
implements|implements
name|Log
block|{
DECL|field|loggedTrace
name|boolean
name|loggedTrace
decl_stmt|;
DECL|field|loggedFatal
name|boolean
name|loggedFatal
decl_stmt|;
DECL|method|isDebugEnabled ()
specifier|public
name|boolean
name|isDebugEnabled
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
DECL|method|isErrorEnabled ()
specifier|public
name|boolean
name|isErrorEnabled
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
DECL|method|isFatalEnabled ()
specifier|public
name|boolean
name|isFatalEnabled
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
DECL|method|isInfoEnabled ()
specifier|public
name|boolean
name|isInfoEnabled
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
DECL|method|isTraceEnabled ()
specifier|public
name|boolean
name|isTraceEnabled
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
DECL|method|isWarnEnabled ()
specifier|public
name|boolean
name|isWarnEnabled
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
DECL|method|trace (Object message)
specifier|public
name|void
name|trace
parameter_list|(
name|Object
name|message
parameter_list|)
block|{
name|loggedTrace
operator|=
literal|true
expr_stmt|;
block|}
DECL|method|trace (Object message, Throwable t)
specifier|public
name|void
name|trace
parameter_list|(
name|Object
name|message
parameter_list|,
name|Throwable
name|t
parameter_list|)
block|{
name|loggedTrace
operator|=
literal|true
expr_stmt|;
block|}
DECL|method|debug (Object message)
specifier|public
name|void
name|debug
parameter_list|(
name|Object
name|message
parameter_list|)
block|{     }
DECL|method|debug (Object message, Throwable t)
specifier|public
name|void
name|debug
parameter_list|(
name|Object
name|message
parameter_list|,
name|Throwable
name|t
parameter_list|)
block|{     }
DECL|method|info (Object message)
specifier|public
name|void
name|info
parameter_list|(
name|Object
name|message
parameter_list|)
block|{     }
DECL|method|info (Object message, Throwable t)
specifier|public
name|void
name|info
parameter_list|(
name|Object
name|message
parameter_list|,
name|Throwable
name|t
parameter_list|)
block|{     }
DECL|method|warn (Object message)
specifier|public
name|void
name|warn
parameter_list|(
name|Object
name|message
parameter_list|)
block|{     }
DECL|method|warn (Object message, Throwable t)
specifier|public
name|void
name|warn
parameter_list|(
name|Object
name|message
parameter_list|,
name|Throwable
name|t
parameter_list|)
block|{     }
DECL|method|error (Object message)
specifier|public
name|void
name|error
parameter_list|(
name|Object
name|message
parameter_list|)
block|{     }
DECL|method|error (Object message, Throwable t)
specifier|public
name|void
name|error
parameter_list|(
name|Object
name|message
parameter_list|,
name|Throwable
name|t
parameter_list|)
block|{      }
DECL|method|fatal (Object message)
specifier|public
name|void
name|fatal
parameter_list|(
name|Object
name|message
parameter_list|)
block|{
name|loggedFatal
operator|=
literal|true
expr_stmt|;
block|}
DECL|method|fatal (Object message, Throwable t)
specifier|public
name|void
name|fatal
parameter_list|(
name|Object
name|message
parameter_list|,
name|Throwable
name|t
parameter_list|)
block|{
name|loggedFatal
operator|=
literal|true
expr_stmt|;
block|}
block|}
end_class

end_unit

