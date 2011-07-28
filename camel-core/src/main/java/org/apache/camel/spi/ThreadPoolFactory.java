begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_package
DECL|package|org.apache.camel.spi
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spi
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ExecutorService
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
name|ScheduledExecutorService
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
name|ThreadFactory
import|;
end_import

begin_comment
comment|/**  * Creates ExecutorService and ScheduledExecutorService objects that work with a thread pool for a given ThreadPoolProfile and ThreadFactory.  *   * This interface allows to customize the creation of these objects to adapt camel for application servers and other environments where thread pools  * should not be created with the jdk methods  */
end_comment

begin_interface
DECL|interface|ThreadPoolFactory
specifier|public
interface|interface
name|ThreadPoolFactory
block|{
DECL|method|newThreadPool (ThreadPoolProfile profile, ThreadFactory threadFactory)
name|ExecutorService
name|newThreadPool
parameter_list|(
name|ThreadPoolProfile
name|profile
parameter_list|,
name|ThreadFactory
name|threadFactory
parameter_list|)
function_decl|;
DECL|method|newScheduledThreadPool (ThreadPoolProfile profile, ThreadFactory threadFactory)
name|ScheduledExecutorService
name|newScheduledThreadPool
parameter_list|(
name|ThreadPoolProfile
name|profile
parameter_list|,
name|ThreadFactory
name|threadFactory
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

